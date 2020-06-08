package cn.ruanyun.backInterface.modules.business.goodService.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.business.goodService.GoodServerVO;
import cn.ruanyun.backInterface.modules.business.goodService.mapper.GoodServiceMapper;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 商品服务接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IGoodServiceServiceImpl extends ServiceImpl<GoodServiceMapper, GoodService> implements IGoodServiceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGoodService(GoodService goodService) {

           if (ToolUtil.isEmpty(goodService.getCreateBy())) {

                       goodService.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       goodService.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(goodService)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGoodService(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 按商品获取服务类型数据
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodServerVO> getGoodsServiceList(String goodsId) {
        List<GoodService> list =this.list(new QueryWrapper<GoodService>().lambda()
                .eq(GoodService::getGoodsId,goodsId)
                .eq(GoodService::getDelFlag,CommonConstant.STATUS_NORMAL)
        );

        List<GoodServerVO> goodsServiceVOS = list.parallelStream().map(goodsService -> {
            GoodServerVO goods = new GoodServerVO();
            ToolUtil.copyProperties(goodsService,goods);
            return  goods;
        }).collect(Collectors.toList());
        return goodsServiceVOS;
    }

    /**
     * 获取商家自己的公用服务数据
     * @return
     */
    @Override
    public List getShopServiceList() {

        return Optional.ofNullable(this.list(new QueryWrapper<GoodService>().lambda()
                .eq(GoodService::getGoodsId,CommonConstant.STATUS_NORMAL)
                .eq(GoodService::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(GoodService::getDelFlag, CommonConstant.STATUS_NORMAL)
                .orderByDesc(GoodService::getCreateTime)
        )).orElse(null);
    }


}