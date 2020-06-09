package cn.ruanyun.backInterface.modules.business.privateNumberAx.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.BackUserInfo;
import cn.ruanyun.backInterface.modules.business.privateNumber.pojo.PrivateNumber;
import cn.ruanyun.backInterface.modules.business.privateNumber.service.IPrivateNumberService;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.mapper.PrivateNumberAxMapper;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.pojo.PrivateNumberAx;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.service.IPrivateNumberAxService;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.util.AXBPrivateNumberUtils;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.vo.PrivateNumberAxVo;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.vo.ReturnJson;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 华为隐私通话接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IPrivateNumberAxServiceImpl extends ServiceImpl<PrivateNumberAxMapper, PrivateNumberAx>
        implements IPrivateNumberAxService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IPrivateNumberService privateNumberService;

    @Autowired
    private IUserService userService;

    @Override
    public Result<Object> getPrivateNumByStoreIdAndUseId(String storeId) {

        BackUserInfo currentUser = securityUtil.getCurrUser();

        Optional<User> storeOption  = Optional.ofNullable(userService.getById(storeId));
        log.info("当前登录id是：" + currentUser.getId());

        //1. 判断当前用户是否已经跟商家绑定了虚拟号段
       return Optional.ofNullable(this.getOne(Wrappers.<PrivateNumberAx>lambdaQuery()
        .eq(PrivateNumberAx::getStoreId, storeId)
        .eq(PrivateNumberAx::getCreateBy, currentUser.getId())))
        .map(privateNumberAx ->

            Optional.ofNullable(privateNumberService.getPrivateNumber(privateNumberAx.getPrivateNumberId()))
                    .map(privateNumber -> new ResultUtil<>().setData(privateNumber, "获取虚拟号成功！"))
                    .orElse(new ResultUtil<>().setErrorMsg(210, "获取虚拟号段失败！"))
        ).orElseGet(() -> storeOption.map(user -> {

            log.info("商家手机号是：" + user.getMobile());
            //2.1 消费者的手机号码
            if (StringUtils.isNotBlank(currentUser.getMobile()) && StringUtils.isNotBlank(user.getMobile())) {

                //消费者的手机号码
                String callerNum = "+86" + currentUser.getMobile();

                //商家的手机号码
                String calleeNum = "+86" + user.getMobile();

                //最新的一个虚拟号段
                PrivateNumber privateNumberNew = privateNumberService.getNotBoundPrivateNumber();

                if (ToolUtil.isEmpty(privateNumberNew)) {

                    return new ResultUtil<>().setErrorMsg(215, "当前暂无可用虚拟号段");
                }

                //2.1 绑定虚拟号段
                ReturnJson result = JSONObject.parseObject(AXBPrivateNumberUtils.axbBindNumber(callerNum, calleeNum, "+86" + privateNumberNew.getPrivateNum(),
                        privateNumberNew.getCityCode()), ReturnJson.class);

                if (ObjectUtil.equal("0", result.getResultcode())) {


                    //2.2 更新该虚拟号段的绑定状态
                    privateNumberNew.setBound(BooleanTypeEnum.YES);
                    privateNumberService.updateById(privateNumberNew);

                    //2.3 储存到数据库
                    PrivateNumberAx privateNumberAx = new PrivateNumberAx();
                    privateNumberAx.setStoreId(storeId)
                            .setPrivateNumberId(privateNumberNew.getId())
                            .setCreateBy(currentUser.getId());
                    ToolUtil.copyProperties(result, privateNumberAx);

                    this.save(privateNumberAx);
                    return new ResultUtil<>().setData(result.getRelationNum(), "获取虚拟号段成功！");
                }else {

                    return new ResultUtil<>().setErrorMsg(Integer.parseInt(result.getResultcode()), result.getResultdesc());
                }

            }else {

                return new ResultUtil<>().setErrorMsg(211, "消费者或者商家号码为空！");
            }

        }).orElse(new ResultUtil<>().setErrorMsg(212, "商家信息为空！")));
    }

    /**
     * 获取虚拟号段绑定关系列表
     *
     * @param pageVo 分页参数
     * @return PrivateNumberAxVo
     */
    @Override
    public DataVo<PrivateNumberAxVo> getPrivateNumberAxVoList(PageVo pageVo) {

        Page<PrivateNumberAx> privateNumberAxPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<PrivateNumberAx>lambdaQuery()
        .orderByDesc(PrivateNumberAx::getCreateTime));

        DataVo<PrivateNumberAxVo> result = new DataVo<>();

        result.setDataResult(Optional.ofNullable(privateNumberAxPage.getRecords())
        .map(privateNumberAxes -> privateNumberAxes.parallelStream().flatMap(privateNumberAx -> {

            PrivateNumberAxVo privateNumberAxVo = new PrivateNumberAxVo();

            //客户
            Optional.ofNullable(userService.getById(privateNumberAx.getCreateBy()))
                    .ifPresent(user -> privateNumberAxVo.setCallerName(user.getNickName())
                            .setCallerPhone(user.getMobile()));

            //商家
            Optional.ofNullable(userService.getById(privateNumberAx.getStoreId()))
                    .ifPresent(user -> privateNumberAxVo.setCalleeName(user.getShopName())
                    .setCalleePhone(user.getMobile()));

            ToolUtil.copyProperties(privateNumberAx, privateNumberAxVo);

            //虚拟号段
            privateNumberAxVo.setPrivateNumber(privateNumberAx.getPrivateNumberId());

            return Stream.of(privateNumberAxVo);
        }).collect(Collectors.toList())).orElse(null))
        .setCurrentPageNum(privateNumberAxPage.getCurrent())
        .setTotalSize(privateNumberAxPage.getTotal());

        return result;
    }

    /**
     * 接触虚拟号段绑定关系
     *
     * @param id id
     */
    @Override
    public Result<Object> unbindPrivateNum(String id) {

        //1. 解除虚拟号段

       return Optional.ofNullable(this.getById(id)).map(privateNumberAx -> {

            ReturnJson result = JSONObject.parseObject(AXBPrivateNumberUtils.axbUnbindNumber(null, privateNumberAx.getSubscriptionId()
            ), ReturnJson.class);

            if (ObjectUtil.equal("0", result.getResultcode())) {

                //2. 更新虚拟号段字段
                Optional.ofNullable(privateNumberService.getById(privateNumberAx.getPrivateNumberId()))
                        .ifPresent(privateNumber -> {

                            privateNumber.setBound(BooleanTypeEnum.NO);
                            privateNumberService.updateById(privateNumber);
                        });

                //3. 移除绑定表
                this.removeById(privateNumberAx.getId());


                return new ResultUtil<>().setSuccessMsg("移除绑定成功！");
            }else {

                return new ResultUtil<>().setErrorMsg(Integer.parseInt(result.getResultcode()), result.getResultdesc());
            }
        }).orElse(new ResultUtil<>().setErrorMsg(201, "不存在此数据！"));
    }
}