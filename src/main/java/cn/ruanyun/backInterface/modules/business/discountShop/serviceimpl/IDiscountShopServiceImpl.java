package cn.ruanyun.backInterface.modules.business.discountShop.serviceimpl;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.utils.LocationUtils;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis.IUserServiceImpl;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.discountShop.DTO.DiscountShopDTO;
import cn.ruanyun.backInterface.modules.business.discountShop.VO.DiscountShopListVO;
import cn.ruanyun.backInterface.modules.business.discountShop.mapper.DiscountShopMapper;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.discountShop.service.IDiscountShopService;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.good.serviceimpl.IGoodServiceImpl;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodCategory.serviceimpl.IGoodCategoryServiceImpl;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.selectStore.service.ISelectStoreService;
import cn.ruanyun.backInterface.modules.business.staffManagement.mapper.StaffManagementMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 优惠券参加的商家接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IDiscountShopServiceImpl extends ServiceImpl<DiscountShopMapper, DiscountShop> implements IDiscountShopService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private UserMapper userMapper;
       @Autowired
       private IUserServiceImpl iUserService;
        @Autowired
        private IGoodCategoryServiceImpl iGoodCategoryServiceImpl;
        @Autowired
        private ICommentService commentService;
        @Autowired
        private IGoodService goodService;
        @Autowired
        private IstoreFirstRateServiceService storeFirstRateServiceService;
        @Autowired
        private IGradeService gradeService;


       @Override
       public void insertOrderUpdateDiscountShop(DiscountShop discountShop) {

           if (ToolUtil.isEmpty(discountShop.getCreateBy())) {

                       discountShop.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       discountShop.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(discountShop)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeDiscountShop(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    /**
     * APP查询优惠券参与的商家列表
     * @return
     */
    @Override
    public List<DiscountShopListVO> getDiscountShopList(DiscountShopDTO discountShopDTO) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<DiscountShop>().lambda().eq(DiscountShop::getDiscountId,discountShopDTO.getDiscountId())
            .orderByDesc(DiscountShop::getCreateBy)
        )))

                .map(discountShops -> discountShops.parallelStream().flatMap(discountShop -> {
                    DiscountShopListVO discountShopListVO = new DiscountShopListVO();

                    User user1 = userMapper.selectById(discountShop.getShopId());

                    ToolUtil.copyProperties(user1,discountShopListVO);

                    //等级
                    discountShopListVO.setStoreLevel(iUserService.judgeStoreLevel(discountShop.getShopId()))

                            //门店星级
                            .setStoreStarLevel(Double.parseDouble(gradeService.getShopScore(discountShop.getShopId())))

                            //优质服务
                            .setFirstRateService(
                                    storeFirstRateServiceService.getStoreFirstRateServiceName(discountShop.getShopId(), CheckEnum.CHECK_SUCCESS)
                            )
                            //信任标识
                            .setTrustIdentity(user1.getTrustIdentity())

                            //连锁认证
                            .setAuthenticationTypeEnum(user1.getAuthenticationTypeEnum())

                            //评价条数
                            .setCommentNum(commentService.getCommentByStore(discountShop.getShopId()))

                            //最低价格
                            .setLowPrice(goodService.getLowPriceByStoreId(discountShop.getShopId()))

                            //距离
                            //.setDistance(LocationUtils.getDistance( storeListDto.getLongitude(),storeListDto.getLatitude(), user.getLongitude(),user.getLatitude()))

                            //商家类型 （1，酒店 2.主持人 3.默认）
                            .setStoreType(iGoodCategoryServiceImpl.judgeStoreType(user1))
                    ;


                    return Stream.of(discountShopListVO);
                }).collect(Collectors.toList())).orElse(null);


    }












}