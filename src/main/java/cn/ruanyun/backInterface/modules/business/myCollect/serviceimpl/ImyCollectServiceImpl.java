package cn.ruanyun.backInterface.modules.business.myCollect.serviceimpl;

import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.myCollect.VO.MyCollectListVO;
import cn.ruanyun.backInterface.modules.business.myCollect.mapper.myCollectMapper;
import cn.ruanyun.backInterface.modules.business.myCollect.pojo.myCollect;
import cn.ruanyun.backInterface.modules.business.myCollect.service.ImyCollectService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 我的收藏接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ImyCollectServiceImpl extends ServiceImpl<myCollectMapper, myCollect> implements ImyCollectService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private GoodMapper iGoodMapper;

       @Override
       public void insertOrderUpdatemyCollect(myCollect myCollect) {

           if (ToolUtil.isEmpty(myCollect.getCreateBy())) {

                       myCollect.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       myCollect.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(myCollect)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removemyCollect(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<MyCollectListVO> myCollectList() {
           //查询用户收藏表中的商品记录
        List<myCollect> myCollectList = this.list(new QueryWrapper<myCollect>().lambda()
                .eq(myCollect::getCreateBy,securityUtil.getCurrUser().getId()).orderByDesc(myCollect::getCreateTime));

            //按商品id查询商品详情
        List<MyCollectListVO> list = myCollectList.parallelStream().map(mc-> {
            MyCollectListVO myCollectListVO = new MyCollectListVO();
            //查询商品详情
            Good good = iGoodMapper.selectById(mc.getGoodId());
            myCollectListVO.setId(mc.getId())
                    .setGoodsId(good.getId())
                    .setGoodName(good.getGoodName())
                    .setGoodPics(good.getGoodPics())
                    .setGoodNewPrice(good.getGoodNewPrice());
            return myCollectListVO;
        }).collect(Collectors.toList());

        return list;
    }

}