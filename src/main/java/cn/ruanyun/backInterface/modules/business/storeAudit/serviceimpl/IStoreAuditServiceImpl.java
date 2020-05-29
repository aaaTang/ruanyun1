package cn.ruanyun.backInterface.modules.business.storeAudit.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.FollowTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditListVO;
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
    private IGoodCategoryService goodCategoryService;
    @Resource
    private IAreaService iAreaService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IGoodsPackageService goodsPackageService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IFollowAttentionService followAttentionService;
    @Autowired
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private IGoodService iGoodService;


    @Override
    public Result<Object> insertOrderUpdateStoreAudit(StoreAudit storeAudit) {

        StoreAudit s = this.getOne(new QueryWrapper<StoreAudit>().lambda().eq(StoreAudit::getCreateBy,securityUtil.getCurrUser().getId()));

        if(ToolUtil.isNotEmpty(s)){
            return new ResultUtil<>().setErrorMsg(201,"您已申请，无需再次申请！");
        }else {
            storeAudit.setCreateBy(securityUtil.getCurrUser().getId());
            return new ResultUtil<>().setData( Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeAudit)))
                    .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                    .toFuture().join(),"申请成功！");
        }

    }

    @Override
    public void removeStoreAudit(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * app商铺信息
     * @return
     */
    @Override
    public StoreAuditListVO getStoreAudisByid(String id) {
        StoreAuditListVO storeAuditListVO = new StoreAuditListVO();
        //获取商家信息
        User byId = userService.getById(id);
        ToolUtil.copyProperties(byId,storeAuditListVO);

        //获取发布的套餐数量
        storeAuditListVO.setGoodsPackageCuount(iGoodService.count(Wrappers.<Good>lambdaQuery().eq(Good::getCreateBy,id).eq(Good::getTypeEnum, GoodTypeEnum.GOODSPACKAGE)));
        //获取关注的数量
        storeAuditListVO.setFollowCount(followAttentionService.count(Wrappers.<FollowAttention>lambdaQuery().eq(FollowAttention::getUserId,id).eq(FollowAttention::getFollowTypeEnum, FollowTypeEnum.Follow_SHOP)));
        //获取评论的数量
        storeAuditListVO.setCommonCount(commentService.count(Wrappers.<Comment>lambdaQuery().eq(Comment::getUserId,id)));

        return storeAuditListVO;
    }

    @Override
    public Result<Object> checkStoreAudit(StoreAuditDTO storeAuditDTO) {

        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(super.getById(storeAuditDTO.getId())))
                .thenApplyAsync(storeAudit -> storeAudit.map(storeAudit1 -> {

                    //1.审核成功 角色转化
                   if (ObjectUtil.equal(storeAuditDTO.getCheckEnum(), CheckEnum.CHECK_SUCCESS)) {

                       //1.1 移除之前的角色
                       userRoleService.remove(Wrappers.<UserRole>lambdaQuery()
                       .eq(UserRole::getUserId, storeAudit1.getCreateBy()));
                       UserRole userRole = new UserRole();
                       userRole.setUserId(storeAudit1.getCreateBy());
                       //1.2 重新分配角色
                       if (ObjectUtil.equal(storeAudit1.getStoreType(), StoreTypeEnum.INDIVIDUALS_IN)) {

                           userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.PER_STORE));
                       }else if (ObjectUtil.equal(storeAudit1.getStoreType(), StoreTypeEnum.MERCHANTS_SETTLED)){

                           userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.STORE));
                       }else {

                           userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.PERSON_STUDIO));
                       }
                       userRoleService.save(userRole);

                       User user = new User();
                       user.setShopName(storeAudit1.getUsername());
                       user.setNickName(storeAudit1.getUsername());
                       user.setId(storeAudit1.getCreateBy());
                       user.setAreaId(storeAudit1.getAreaId());
                       user.setAddress(Optional.ofNullable(iAreaService.getAddress(storeAudit1.getAreaId())).orElse("地区暂未设置！"));
                       user.setClassId(storeAudit1.getClassificationId());
                       user.setWechatAccount(storeAudit1.getWechatAccount());
                       user.setAlipayAccount(storeAudit1.getAlipayAccount());
                       userMapper.updateById(user);
                   }

                    ToolUtil.copyProperties(storeAuditDTO, storeAudit1);
                   super.updateById(storeAudit1);

                   return new ResultUtil<>().setSuccessMsg("审核成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无该数据！")))
                .join();
    }


    @Override
    public List<StoreAuditVO> getStoreAuditList(StoreAuditDTO storeAuditDTO) {
        return CompletableFuture.supplyAsync(() -> {
            //组合条件搜索
            LambdaQueryWrapper<StoreAudit> wrapper = new LambdaQueryWrapper<>();
            if (ToolUtil.isNotEmpty(storeAuditDTO.getMobile())) {
                wrapper.like(StoreAudit::getMobile, storeAuditDTO.getMobile());
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getId())) {
                wrapper.and(w -> w.eq(StoreAudit::getCreateBy, storeAuditDTO.getId()));
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getCheckEnum())) {
                wrapper.and(w -> w.eq(StoreAudit::getCheckEnum, storeAuditDTO.getCheckEnum()));
            }
            if (ToolUtil.isNotEmpty(storeAuditDTO.getUsername())) {
                wrapper.and(w -> w.like(StoreAudit::getUsername, storeAuditDTO.getUsername()));
            }
            wrapper.orderByAsc(StoreAudit::getCheckEnum).orderByDesc(StoreAudit::getCreateTime);
            return super.list(wrapper);
        }).thenApplyAsync(storeAudit -> {
            //封装查寻数据
            return storeAudit.parallelStream().map(sa -> Optional.ofNullable(sa).map(s -> {
                StoreAuditVO storeAuditVO = new StoreAuditVO();
                ToolUtil.copyProperties(s, storeAuditVO);
                //查询服务类型
                storeAuditVO.setClassificationName(Optional.ofNullable(goodCategoryService.getGoodCategoryName(s.getClassificationId())).orElse("未知"));
                //查询区域
                storeAuditVO.setAreaName(Optional.ofNullable(iAreaService.getAddress(s.getAreaId())).orElse("未知"));
                //商家店铺轮播图
                storeAuditVO.setPics(Optional.ofNullable(userMapper.selectById(s.getCreateBy())).map(User::getPic).orElse("暂无"));
                return storeAuditVO;

            }).orElse(null)).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());
        }).join();
    }
}