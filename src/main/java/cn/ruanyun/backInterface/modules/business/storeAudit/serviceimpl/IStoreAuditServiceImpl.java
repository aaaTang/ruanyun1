package cn.ruanyun.backInterface.modules.business.storeAudit.serviceimpl;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserRoleMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.mapper.BestShopMapper;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo.BestShop;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.service.IBestShopService;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.serviceimpl.IBestShopServiceImpl;
import cn.ruanyun.backInterface.modules.business.classification.mapper.ClassificationMapper;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;


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
    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private GoodsPackageMapper igoodsPackageMapper;
    @Resource
    private BestShopMapper ibestShopMapper;
    @Resource
    private IBestShopService iBestShopService;


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
                .thenApplyAsync(storeAudit -> storeAudit.map(sa -> {
                    //如果审核通过
                    if (CheckEnum.CHECK_SUCCESS.equals(storeAuditDTO.getCheckEnum())) {
                        //修改用户角色
                        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, sa.getCreateBy()));
                        if(ToolUtil.isNotEmpty(userRoles)){
                            userRoleMapper.deleteBatchIds(userRoles.stream().map(UserRole::getId).collect(Collectors.toList()));
                        }
                        UserRole userRole = new UserRole();
                        String roleId = StoreTypeEnum.MERCHANTS_SETTLED.equals(sa.getStoreType())? "496138616573954" : "496138616573955";
                        userRole.setRoleId(roleId)
                                .setUserId(sa.getCreateBy());
                        //修改用户表信息
                        ShopParticularsDTO shopParticularsDTO = new ShopParticularsDTO();
                            shopParticularsDTO.setId(userRole.getUserId())
                                    .setShopName(sa.getUsername())
                                    .setLatitude(sa.getLatitude())
                                    .setLongitude(sa.getLongitude())
                                    .setMobile(sa.getMobile())
                                    .setPic(sa.getPic());

                        igoodsPackageMapper.UpdateShopParticulars(shopParticularsDTO);


                        //删除缓存
                        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
                        assert keysUserRole != null;
                        redisTemplate.delete(keysUserRole);
                    }
                    ToolUtil.copyProperties(storeAuditDTO, sa);
                    super.updateById(sa);
                    return new ResultUtil<>().setSuccessMsg("审核成功");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "审核失败"))).join();
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

    @Override
    public Result<Object> addApply(StoreAudit storeAudit) {
        storeAudit.setCreateBy(securityUtil.getCurrUser().getId());

        BestShop bestShop  = ibestShopMapper.selectOne(new QueryWrapper<BestShop>().lambda().eq(BestShop::getStoreAuditId,securityUtil.getCurrUser().getId()));

            if(ToolUtil.isEmpty(bestShop)){
                BestShop bestShop1=new BestShop();
                bestShop1.setStoreAuditId(securityUtil.getCurrUser().getId())
                        .setStrict(0)
                        .setCreateBy(securityUtil.getCurrUser().getId());
                iBestShopService.insertOrderUpdateBestShop(bestShop1);
            }

        return CompletableFuture.supplyAsync(() ->
                //查询是否有过申请记录
                super.list(new LambdaQueryWrapper<StoreAudit>().eq(StoreAudit::getCreateBy, storeAudit.getCreateBy()).and(wrapper -> wrapper.eq(StoreAudit::getCheckEnum, CheckEnum.PRE_CHECK).or().eq(StoreAudit::getCheckEnum, CheckEnum.CHECK_SUCCESS)))
        ).thenApplyAsync(storeAudits -> {
            if (storeAudits.size() > 0) {
                return new ResultUtil<>().setErrorMsg(201, "已提交审核请耐心等待");
            } else {
                super.save(storeAudit);
                return new ResultUtil<>().setData(storeAudit);
            }
        }).join();
    }

}