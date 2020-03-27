package cn.ruanyun.backInterface.modules.business.storeAudit.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * 商家入驻审核接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IStoreAuditServiceImpl extends ServiceImpl<StoreAuditMapper, StoreAudit> implements IStoreAuditService {


    @Autowired
    private SecurityUtil securityUtil;
    @Resource
    private IClassificationService iClassificationService;
    @Resource
    private IAreaService iAreaService;


    @Override
    public void insertOrderUpdateStoreAudit(StoreAudit storeAudit) {

        if (ToolUtil.isEmpty(storeAudit.getCreateBy())) {

            storeAudit.setCreateBy(securityUtil.getCurrUser().getId());
        } else {

            storeAudit.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeAudit)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeStoreAudit(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public Result<Object> checkStoreAudit(StoreAuditDTO storeAuditDTO) {

        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(super.getById(storeAuditDTO.getId())))
                .thenApplyAsync(storeAudit -> storeAudit.map(storeAudit1 -> {

                    //1.审核成功 角色转化
                   if (ObjectUtil.equal(storeAuditDTO.getCheckEnum(), CheckEnum.CHECK_SUCCESS)) {

                       //1.1 移除之前的角色




                   }



                    //2. 审核失败，极光推送

                }))
    }


    @Override
    public List<StoreAuditVO> getStoreAuditList(StoreAuditDTO storeAuditDTO) {
        return CompletableFuture.supplyAsync(() -> {
            //组合条件搜索
            LambdaQueryWrapper<StoreAudit> wrapper = new LambdaQueryWrapper<>();
            if (ToolUtil.isNotEmpty(storeAuditDTO.getMobile())) {
                wrapper.eq(StoreAudit::getMobile, storeAuditDTO.getMobile());
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getId())) {
                wrapper.and(w -> w.eq(StoreAudit::getCreateBy, storeAuditDTO.getId()));
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getCheckEnum())) {
                wrapper.and(w -> w.eq(StoreAudit::getCheckEnum, storeAuditDTO.getCheckEnum()));
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getUsername())) {
                wrapper.and(w -> w.eq(StoreAudit::getUsername, storeAuditDTO.getUsername()));
            }
            wrapper.orderByAsc(StoreAudit::getCheckEnum).orderByDesc(StoreAudit::getCreateTime);
            return super.list(wrapper);
        }).thenApplyAsync(storeAudit -> {
            //封装查寻数据
            return storeAudit.parallelStream().map(sa -> Optional.ofNullable(sa).map(s -> {
                StoreAuditVO storeAuditVO = new StoreAuditVO();
                ToolUtil.copyProperties(s, storeAuditVO);
                //查询服务类型
                storeAuditVO.setClassificationName(Optional.ofNullable(iClassificationService.getClassificationName(s.getClassificationId())).orElse("未知"));
                //查询区域
                storeAuditVO.setAreaName(Optional.ofNullable(iAreaService.getAddress(s.getAreaId())).orElse("未知"));
                return storeAuditVO;
            }).orElse(null)).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());
        }).join();
    }
}