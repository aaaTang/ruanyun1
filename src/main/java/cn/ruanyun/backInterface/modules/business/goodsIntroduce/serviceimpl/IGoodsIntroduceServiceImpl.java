package cn.ruanyun.backInterface.modules.business.goodsIntroduce.serviceimpl;

import cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO.GoodsIntroduceVO;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.mapper.GoodsIntroduceMapper;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.pojo.GoodsIntroduce;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.service.IGoodsIntroduceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 商品介绍接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IGoodsIntroduceServiceImpl extends ServiceImpl<GoodsIntroduceMapper, GoodsIntroduce> implements IGoodsIntroduceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGoodsIntroduce(GoodsIntroduce goodsIntroduce) {

           if (ToolUtil.isEmpty(goodsIntroduce.getCreateBy())) {

                       goodsIntroduce.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       goodsIntroduce.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(goodsIntroduce)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGoodsIntroduce(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 按商品id查询商品介绍
     * @param goodsPackageId
     * @return
     */
    @Override
    public List goodsIntroduceList(String ids,String goodsPackageId,Integer introduceAndDuy) {

           List<GoodsIntroduce> goodsIntroduceList = this.list(new QueryWrapper<GoodsIntroduce>().lambda()
            .eq(ToolUtil.isNotEmpty(ids),GoodsIntroduce::getId,ids)
            .eq(ToolUtil.isNotEmpty(goodsPackageId),GoodsIntroduce::getGoodsPackageId,goodsPackageId)
                   .eq(ToolUtil.isNotEmpty(introduceAndDuy),GoodsIntroduce::getIntroduceAndDuy,introduceAndDuy)
                   .eq(GoodsIntroduce::getDelFlag,0)
                   .orderByAsc(GoodsIntroduce::getCreateTime)
           );

        List<GoodsIntroduceVO> goodsIntroduceVOList = new ArrayList<>();
        for (GoodsIntroduce goodsIntroduce : goodsIntroduceList) {
            GoodsIntroduceVO gvo = new GoodsIntroduceVO();
            ToolUtil.copyProperties(goodsIntroduce,gvo);
            goodsIntroduceVOList.add(gvo);
        }
        return Optional.ofNullable(goodsIntroduceVOList).orElse(null);
    }


}