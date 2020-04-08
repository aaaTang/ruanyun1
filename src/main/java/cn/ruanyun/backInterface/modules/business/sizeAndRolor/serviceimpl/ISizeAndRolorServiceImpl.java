package cn.ruanyun.backInterface.modules.business.sizeAndRolor.serviceimpl;

import cn.ruanyun.backInterface.modules.business.color.entity.Color;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.SizeAndRolorVO;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.SizeVO;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 规格和大小接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class ISizeAndRolorServiceImpl extends ServiceImpl<SizeAndRolorMapper, SizeAndRolor> implements ISizeAndRolorService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSizeAndRolor(SizeAndRolor sizeAndRolor) {

           if (ToolUtil.isEmpty(sizeAndRolor.getCreateBy())) {

                       sizeAndRolor.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       sizeAndRolor.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(sizeAndRolor)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeSizeAndRolor(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }



    /**
     * 获取商品规格和大小
     * @return
     */
      @Override
      public List<SizeAndRolorVO> SizeAndRolorList(String goodsId,String sizeId) {

           //获取一级颜色数据
            List<SizeAndRolor>  list = this.list(new QueryWrapper<SizeAndRolor>().lambda()
                    .eq(SizeAndRolor::getGoodsId,goodsId).eq(SizeAndRolor::getIsParent,1));

            List<SizeAndRolorVO> sizeAndRolorList  = list.parallelStream().map(sizeAndRolor -> {
                SizeAndRolorVO sr = new SizeAndRolorVO();

                //获取颜色下的二级数据
                List<SizeAndRolor>  sizeVO = this.list(new QueryWrapper<SizeAndRolor>().lambda()
                        .eq(SizeAndRolor::getParentId,sizeAndRolor.getId()).eq(SizeAndRolor::getGoodsId,goodsId)
                        .eq(SizeAndRolor::getIsParent,0));

                AtomicReference<Integer> num = new AtomicReference<>(0);

                    //循环二级获取尺寸
                    List<SizeVO> sizeVOList = sizeVO.parallelStream().map(srv ->{
                        SizeVO sizeVO1 = new SizeVO();
                            if(ToolUtil.isNotEmpty(sizeId)){
                                if(sizeId.equals(srv.getId())){
                                    num.updateAndGet(v -> v + srv.getInventory());//单个尺寸的库存数量
                                }
                            }else {
                                num.updateAndGet(v -> v + srv.getInventory());//每种颜色的总和库存数量
                            }

                        ToolUtil.copyProperties(srv,sizeVO1);
                        return sizeVO1;
                    }).collect(Collectors.toList());

                sr.setSizeList(sizeVOList);//尺寸的集合
                sr.setInventory(num);//库存数量

                ToolUtil.copyProperties(sizeAndRolor,sr);
                return sr;
            }).collect(Collectors.toList());

         return sizeAndRolorList;
      }


    @Override
    public String getSizeName(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(SizeAndRolor::getSize).orElse(null);
    }

    @Override
    public String getColorName(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(SizeAndRolor::getColor).orElse(null);
    }

    @Override
    public Integer getInventory(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(SizeAndRolor::getInventory).orElse(null);
    }




}