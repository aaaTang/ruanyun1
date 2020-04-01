package cn.ruanyun.backInterface.modules.business.myFootprint.serviceimpl;

import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.myCollect.VO.MyCollectListVO;
import cn.ruanyun.backInterface.modules.business.myFootprint.VO.MyFootprintVO;
import cn.ruanyun.backInterface.modules.business.myFootprint.mapper.MyFootprintMapper;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户足迹接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IMyFootprintServiceImpl extends ServiceImpl<MyFootprintMapper, MyFootprint> implements IMyFootprintService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private GoodMapper iGoodMapper;

       @Override
       public void insertOrderUpdateMyFootprint(MyFootprint myFootprint) {


//           if (ToolUtil.isEmpty(myFootprint.getCreateBy())) {
//
//                       myFootprint.setCreateBy(securityUtil.getCurrUser().getId());
//                   }else {
//
//                       myFootprint.setUpdateBy(securityUtil.getCurrUser().getId());
//                   }
            //查询表中是否存在已有历史记录
           MyFootprint myf = this.getOne(Wrappers.<MyFootprint>lambdaQuery()
                   .eq(MyFootprint::getCreateBy,securityUtil.getCurrUser().getId()).eq(MyFootprint::getGoodsId,myFootprint.getGoodsId()));

           if(ToolUtil.isEmpty(myf)){
               myFootprint.setCreateBy(securityUtil.getCurrUser().getId());
           }else {
               myFootprint.setCreateTime(new Date());
               myFootprint.setId(myf.getId());
           }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(myFootprint)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeMyFootprint(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<MyFootprintVO> MyFootprintList() {
           //查詢我的足迹列表
        List<MyFootprint> myFootprints = this.list(new QueryWrapper<MyFootprint>().lambda()
                .eq(MyFootprint::getCreateBy,securityUtil.getCurrUser().getId()).orderByDesc(MyFootprint::getCreateTime));

        //按商品id查询商品详情
        List<MyFootprintVO> list = myFootprints.parallelStream().map(mf-> {
            MyFootprintVO myFootprintVO = new MyFootprintVO();
            //查询商品详情
            Good good = iGoodMapper.selectById(mf.getGoodsId());
            myFootprintVO.setId(mf.getId())
                    .setGoodsId(good.getId())
                    .setGoodName(good.getGoodName())
                    .setGoodPics(good.getGoodPics())
                    .setGoodNewPrice(good.getGoodNewPrice());
            return myFootprintVO;
        }).collect(Collectors.toList());
           return list;
    }

}