package cn.ruanyun.backInterface.modules.business.shopWorks.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.business.good.serviceimpl.IGoodServiceImpl;
import cn.ruanyun.backInterface.modules.business.shopWorks.DTO.ShopWorksDTO;
import cn.ruanyun.backInterface.modules.business.shopWorks.VO.AppGetShopWorksListVO;
import cn.ruanyun.backInterface.modules.business.shopWorks.mapper.ShopWorksMapper;
import cn.ruanyun.backInterface.modules.business.shopWorks.pojo.ShopWorks;
import cn.ruanyun.backInterface.modules.business.shopWorks.service.IShopWorksService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 商家作品接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IShopWorksServiceImpl extends ServiceImpl<ShopWorksMapper, ShopWorks> implements IShopWorksService {


       @Autowired
       private SecurityUtil securityUtil;

       @Autowired
       private IGoodServiceImpl iGoodService;

       @Resource
       private UserMapper userMapper;

       @Override
       public void insertOrderUpdateShopWorks(ShopWorks shopWorks) {

           if (ToolUtil.isEmpty(shopWorks.getCreateBy())) {

                       shopWorks.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       shopWorks.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(shopWorks)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeShopWorks(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    /**
     * App获取商家作品列表
     * @param shopWorksDTO 实体类
     * @return
     */
      @Override
      public List<AppGetShopWorksListVO> AppGetShopWorksList(ShopWorksDTO shopWorksDTO){

           return Optional.ofNullable(ToolUtil.setListToNul(
                   this.list(new QueryWrapper<ShopWorks>().lambda()
                           .eq(ToolUtil.isNotEmpty(shopWorksDTO.getCreateBy()),ShopWorks::getCreateBy,shopWorksDTO.getCreateBy())
                           .eq(ToolUtil.isNotEmpty(shopWorksDTO.getId()),ShopWorks::getId,shopWorksDTO.getId())
                           .eq(ShopWorks::getDelFlag, CommonConstant.STATUS_NORMAL)
                           .orderByDesc(ShopWorks::getCreateTime)

           ))).map(shopWorks -> shopWorks.parallelStream().flatMap(shopWorks1 -> {
               AppGetShopWorksListVO appGetShopWorksListVO = new AppGetShopWorksListVO();

               ToolUtil.copyProperties(shopWorks1,appGetShopWorksListVO);

               appGetShopWorksListVO.setShopName(Optional.ofNullable(userMapper.selectById(shopWorks1.getCreateBy())).map(User::getShopName).orElse("商家名称不完整！"));

               return Stream.of(appGetShopWorksListVO);
           }).collect(Collectors.toList())).orElse(null);
      }


    /**
     * 后端获取商家作品列表
     * @return
     */
    @Override
    public List<PcShopWorksListVO> getShopWorksList(ShopWorksDTO shopWorksDTO){

        String userRole =  iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

        return Optional.ofNullable(ToolUtil.setListToNul(
                this.list(new QueryWrapper<ShopWorks>().lambda()
                        .eq(userRole.equals(CommonConstant.STORE)||userRole.equals(CommonConstant.PER_STORE),ShopWorks::getCreateBy,securityUtil.getCurrUser().getId())
                        .eq(ToolUtil.isNotEmpty(shopWorksDTO.getId()),ShopWorks::getId,shopWorksDTO.getId())
                        .eq(ToolUtil.isNotEmpty(shopWorksDTO.getDelFlag()),ShopWorks::getDelFlag,shopWorksDTO.getDelFlag())
                        .orderByDesc(ShopWorks::getCreateTime)

                ))).map(shopWorks -> shopWorks.parallelStream().flatMap(shopWorks1 -> {

                    PcShopWorksListVO pcShopWorksListVO = new PcShopWorksListVO();

                    ToolUtil.copyProperties(shopWorks1,pcShopWorksListVO);
                    //店铺名称
                    pcShopWorksListVO.setShopName(Optional.ofNullable(userMapper.selectById(shopWorks1.getCreateBy())).map(User::getShopName).orElse("商家名称不完整！"));

                return Stream.of(pcShopWorksListVO);
        }).collect(Collectors.toList())).orElse(null);
    }


}