package cn.ruanyun.backInterface.modules.business.privateNumber.serviceimpl;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.privateNumber.mapper.PrivateNumberMapper;
import cn.ruanyun.backInterface.modules.business.privateNumber.pojo.PrivateNumber;
import cn.ruanyun.backInterface.modules.business.privateNumber.service.IPrivateNumberService;
import cn.ruanyun.backInterface.modules.business.privateNumber.vo.PrivateNumberVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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


/**
 * 虚拟号接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IPrivateNumberServiceImpl extends ServiceImpl<PrivateNumberMapper, PrivateNumber> implements IPrivateNumberService {

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 添加或者修改虚拟号段
     *
     * @param privateNumber 实体
     */
    @Override
    public void insertOrUpdatePrivateNumber(PrivateNumber privateNumber) {

       if (ToolUtil.isNotEmpty(privateNumber.getId())) {

           privateNumber.setUpdateBy(securityUtil.getCurrUser().getId());
       }else {

           privateNumber.setCreateBy(securityUtil.getCurrUser().getId());
       }

       this.saveOrUpdate(privateNumber);

    }

    /**
     * 移除虚拟号段
     *
     * @param ids ids集合
     */
    @Override
    public void removePrivateNumber(String ids) {

        this.removeByIds(ToolUtil.splitterStr(ids));
    }

    /**
     * 获取虚拟号段列表
     *
     * @param pageVo 分页参数
     * @return PrivateNumber
     */
    @Override
    public DataVo<PrivateNumberVo> getPrivateNumberList(PageVo pageVo) {

        //分页参数
        Page<PrivateNumber> privateNumberPage = this.page(PageUtil.initMpPage(pageVo),
                Wrappers.<PrivateNumber>lambdaQuery().orderByDesc(PrivateNumber::getCreateTime));


        DataVo<PrivateNumberVo> privateNumberVoDataVo = new DataVo<>();
        //实体数据
        privateNumberVoDataVo.setDataResult(Optional.ofNullable(ToolUtil.setListToNul(privateNumberPage.getRecords())).map(privateNumbers ->
                privateNumbers.parallelStream().flatMap(privateNumber -> {

                    PrivateNumberVo privateNumberVo = new PrivateNumberVo();
                    ToolUtil.copyProperties(privateNumber, privateNumberVo);
                    return Stream.of(privateNumberVo);
                }).collect(Collectors.toList()))
                .orElse(null)).setCurrentPageNum(privateNumberPage.getCurrent())
                .setTotalSize(privateNumberPage.getTotal());

        return privateNumberVoDataVo;
    }

    @Override
    public String getPrivateNumber(String privateNumberId) {

        return Optional.ofNullable(this.getById(privateNumberId))
                .map(PrivateNumber::getPrivateNum)
                .orElse(null);
    }

    @Override
    public PrivateNumber getNotBoundPrivateNumber() {

        return Optional.ofNullable(this.getOne(Wrappers.<PrivateNumber>lambdaQuery()
                .eq(PrivateNumber::getBound, BooleanTypeEnum.NO)
                .orderByDesc(PrivateNumber::getCreateTime)
                .last("limit 1")))
                .orElse(null);
    }

    @Override
    public String getIdByPrivateNum(String privateNumber) {

        return Optional.ofNullable(this.getOne(Wrappers.<PrivateNumber>lambdaQuery()
        .eq(PrivateNumber::getPrivateNum, privateNumber)))
        .map(PrivateNumber::getId)
        .orElse(null);
    }
}