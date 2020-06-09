package cn.ruanyun.backInterface.modules.business.discountShop.serviceimpl;

import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.discountShop.DTO.DiscountShopDTO;
import cn.ruanyun.backInterface.modules.business.discountShop.VO.DiscountShopListVO;
import cn.ruanyun.backInterface.modules.business.discountShop.mapper.DiscountShopMapper;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.discountShop.service.IDiscountShopService;
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

    @Override
    public List<DiscountShopListVO> getDiscountShopList(DiscountShopDTO discountShopDTO) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<DiscountShop>().lambda().eq(DiscountShop::getDiscountId,discountShopDTO.getDiscountId())
            .orderByDesc(DiscountShop::getCreateBy)
        )))

                .map(discountShops -> discountShops.parallelStream().flatMap(discountShop -> {
                    DiscountShopListVO discountShopListVO = new DiscountShopListVO();

                    ToolUtil.copyProperties(discountShop,discountShopListVO);
                    discountShopListVO.setShopName(Optional.ofNullable(userMapper.selectById(discountShop.getShopId())).map(User::getShopName).orElse("商家店铺空！"));

                    return Stream.of(discountShopListVO);
                }).collect(Collectors.toList())).orElse(null);


    }












}