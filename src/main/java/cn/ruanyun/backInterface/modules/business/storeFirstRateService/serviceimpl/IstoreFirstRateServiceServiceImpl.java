package cn.ruanyun.backInterface.modules.business.storeFirstRateService.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.firstRateService.mapper.FirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.DTO.StoreFirstRateServiceDTO;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.VO.StoreFirstRateServiceVO;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.mapper.StoreFirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 商家优质服务接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IstoreFirstRateServiceServiceImpl extends ServiceImpl<StoreFirstRateServiceMapper, StoreFirstRateService> implements IstoreFirstRateServiceService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFirstRateServiceService firstRateServiceService;

    @Autowired
    private IGoodService iGoodService;

    @Resource
    private FirstRateServiceMapper firstRateServiceMapper;

    @Override
    public void insertOrderUpdateStoreFirstRateService(StoreFirstRateService storeFirstRateService) {

        if (ToolUtil.isEmpty(storeFirstRateService.getCreateBy())) {

            storeFirstRateService.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            storeFirstRateService.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeFirstRateService)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeStoreFirstRateService(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public Result<Object> checkStoreFirstRate(StoreFirstRateService storeFirstRateService) {

       return Optional.ofNullable(this.getById(storeFirstRateService.getId())).map(storeFirstRateServiceUpdate -> {

            if (ObjectUtil.equal(CheckEnum.CHECK_SUCCESS, storeFirstRateService.getCheckStatus())) {

                //商家分值加一
                Optional.ofNullable(userService.getById(storeFirstRateService.getCreateBy())).ifPresent(user -> {

                    user.setScore(user.getScore() + 1);
                    userService.updateById(user);
                });

            }

            //2. 赋值操作
            ToolUtil.copyProperties(storeFirstRateService, storeFirstRateServiceUpdate);
            this.updateById(storeFirstRateServiceUpdate);

            return new ResultUtil<>().setSuccessMsg("审核成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(202, "该记录不存在！"));

    }


    /**
     * 获取商家请申请优质服务的记录列表
     * @param storeFirstRateServiceDTO 实体类
     * @return
     */
    @Override
    public List<StoreFirstRateServiceVO> getStoreFirstRateService(StoreFirstRateServiceDTO storeFirstRateServiceDTO){

        //角色权限
       String  userRole = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());



        return Optional.ofNullable(this.list(new QueryWrapper<StoreFirstRateService>().lambda()

            .eq(ToolUtil.isNotEmpty(storeFirstRateServiceDTO.getId()),StoreFirstRateService::getId,storeFirstRateServiceDTO.getId())

             //判断角色是商家或者个人商家
            .eq(userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE),StoreFirstRateService::getCreateBy,securityUtil.getCurrUser().getId())

            .eq(ToolUtil.isNotEmpty(storeFirstRateServiceDTO.getFirstRateServiceIds()),StoreFirstRateService::getFirstRateServiceIds,storeFirstRateServiceDTO.getFirstRateServiceIds())

            .eq(ToolUtil.isNotEmpty(storeFirstRateServiceDTO.getCheckStatus()),StoreFirstRateService::getCheckStatus,storeFirstRateServiceDTO.getCheckStatus())

            .orderByAsc(StoreFirstRateService::getCreateTime)

        )).map(firstRateServiceList -> firstRateServiceList.parallelStream().flatMap(storeFirstRateService1 ->{

                    StoreFirstRateServiceVO storeFirstRateServiceVO = new StoreFirstRateServiceVO();
                        ToolUtil.copyProperties(storeFirstRateService1,storeFirstRateServiceVO);
                        //标签名称
                        storeFirstRateServiceVO.setItemName(Optional.ofNullable(firstRateServiceMapper.selectById(storeFirstRateService1.getFirstRateServiceIds())).map(FirstRateService::getItemName).orElse("-"));
                        //商家名称
                        storeFirstRateServiceVO.setShopName(userService.getUserIdByUserName(storeFirstRateService1.getCreateBy()));


                    return Stream.of(storeFirstRateServiceVO);

                }).collect(Collectors.toList()))
                .orElse(null);
    }



    /**
     * 获取商家的优质服务名称
     * @param createBy  创建人
     * @param checkEnum 状态
     * @return
     */
    @Override
    public List<String> getStoreFirstRateServiceName(String createBy,CheckEnum checkEnum){


        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<StoreFirstRateService>().lambda()

                .eq(ToolUtil.isNotEmpty(createBy),StoreFirstRateService::getCreateBy,createBy)

                .eq(ToolUtil.isNotEmpty(checkEnum),StoreFirstRateService::getCheckStatus,checkEnum)

                .orderByDesc(StoreFirstRateService::getCreateTime)

        ))).map(firstRateServiceList -> firstRateServiceList.parallelStream().flatMap(storeFirstRateService1 ->{

            StoreFirstRateServiceVO storeFirstRateServiceVO = new StoreFirstRateServiceVO();
            ToolUtil.copyProperties(storeFirstRateService1,storeFirstRateServiceVO);
            //标签名称
            storeFirstRateServiceVO.setItemName(Optional.ofNullable(firstRateServiceMapper.selectById(storeFirstRateService1.getFirstRateServiceIds())).map(FirstRateService::getItemName).orElse("-"));

            return Stream.of(storeFirstRateServiceVO);

        }).map(StoreFirstRateServiceVO::getItemName).collect(Collectors.toList()))
                .orElse(null);
    }

}