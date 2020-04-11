package cn.ruanyun.backInterface.modules.business.sizeAndRolor.serviceimpl;

import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.VO.ItemAttrKeyVO;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.VO.ItemAttrValVO;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.inventoryVO;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


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

       @Resource
       private ItemAttrKeyMapper itemAttrKeyMapper;

       @Resource
       private ItemAttrValMapper itemAttrValMapper;

       @Resource
       private GoodMapper goodMapper;

       @Resource
       private SizeAndRolorMapper sizeAndRolorMapper;

       @Override
       public void insertOrderUpdateSizeAndRolor(SizeAndRolor sizeAndRolor) {

           if (ToolUtil.isEmpty(sizeAndRolor.getCreateBy())) {
               sizeAndRolor.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
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

    @Override
    public Map<String,Object> SizeAndRolorList(String goodsId) {

        Map<String,Object> map = new HashMap<>();
           //获取规格数据
        List<ItemAttrKey> itemAttrKey = itemAttrKeyMapper.selectList(new QueryWrapper<ItemAttrKey>().lambda()
                        .eq(ItemAttrKey::getGoodsId,goodsId).eq(ItemAttrKey::getDelFlag,0));
         List<ItemAttrKeyVO> itemAttrKeyVO = itemAttrKey.parallelStream().map(itemAttrKey1 -> {
             ItemAttrKeyVO itemAttr = new ItemAttrKeyVO();
             ToolUtil.copyProperties( itemAttrKey1,itemAttr);
             return itemAttr;
         }).collect(Collectors.toList());

         //获取规格的属性
        for (ItemAttrKeyVO ikey : itemAttrKeyVO) {
            List<ItemAttrVal> itemAttrVal = itemAttrValMapper.selectList(new QueryWrapper<ItemAttrVal>().lambda()
                    .eq(ItemAttrVal::getAttrId,ikey.getId()).eq(ItemAttrVal::getDelFlag,0));
            List<ItemAttrValVO> itemAttrValVO = itemAttrVal.parallelStream().map(itemAttrVal1 -> {
                ItemAttrValVO itemVal = new ItemAttrValVO();
                ToolUtil.copyProperties( itemAttrVal1,itemVal);
                return itemVal;
            }).collect(Collectors.toList());
            ikey.setVal(itemAttrValVO);
        }
        map.put("itemAttrKeyVO",itemAttrKeyVO);

        //获取商品
        Good good =goodMapper.selectOne(Wrappers.<Good>lambdaQuery().eq(Good::getId,goodsId));
        map.put("goodsPrice",good.getGoodNewPrice());//商品价格


       List<SizeAndRolor> list = sizeAndRolorMapper.selectList(
                new QueryWrapper<SizeAndRolor>().lambda().eq(SizeAndRolor::getGoodsId,goodsId));
            Integer inventory = 0;
        for (SizeAndRolor s : list) {
            inventory+=s.getInventory();
        }
        map.put("pic",list.get(0).getPic());//商品图片
        map.put("inventory",inventory);//商品库存
        return map;
    }

    @Override
    public Map<String,Object> getInventory(String ids) {

        Map<String,Object> map = new HashMap<>();
        SizeAndRolor s =  this.getOne(Wrappers.<SizeAndRolor>lambdaQuery().eq(SizeAndRolor::getAttrSymbolPath,ids));
            map.put("inventory",s.getInventory());
            map.put("goodsPrice",s.getGoodsPrice());
            map.put("pic",s.getPic());
        return map;
    }


//
//    /**
//     * 获取商品规格和大小
//     * @return
//     */
//      @Override
//      public List<SizeAndRolorVO> SizeAndRolorList(String goodsId,String sizeId) {
//
//           //获取一级颜色数据
//            List<SizeAndRolor>  list = this.list(new QueryWrapper<SizeAndRolor>().lambda()
//                    .eq(SizeAndRolor::getGoodsId,goodsId).eq(SizeAndRolor::getIsParent,1));
//
//            List<SizeAndRolorVO> sizeAndRolorList  = list.parallelStream().map(sizeAndRolor -> {
//                SizeAndRolorVO sr = new SizeAndRolorVO();
//
//                //获取颜色下的二级数据
//                List<SizeAndRolor>  sizeVO = this.list(new QueryWrapper<SizeAndRolor>().lambda()
//                        .eq(SizeAndRolor::getParentId,sizeAndRolor.getId()).eq(SizeAndRolor::getGoodsId,goodsId)
//                        .eq(SizeAndRolor::getIsParent,0));
//
//                AtomicReference<Integer> num = new AtomicReference<>(0);
//
//                AtomicReference<BigDecimal> goodsPrice =null;
//                    //循环二级获取尺寸
//                    List<SizeVO> sizeVOList = sizeVO.parallelStream().map(srv ->{
//                        SizeVO sizeVO1 = new SizeVO();
//                            if(ToolUtil.isNotEmpty(sizeId)){
//                                if(sizeId.equals(srv.getId())){
//                                    num.updateAndGet(v -> v + srv.getInventory());//单个尺寸的库存数量
//                                }
//
//                                if(ToolUtil.isNotEmpty(sizeAndRolor.getGoodsPrice())) { //判断规格价格不能为空
//                                    goodsPrice.set(srv.getGoodsPrice());//获取二级规格和尺寸价格
//                                }
//                            }else {
//                                num.updateAndGet(v -> v + srv.getInventory());//每种颜色的总和库存数量
//
//                                if(ToolUtil.isNotEmpty(sizeAndRolor.getGoodsPrice())){//判断规格价格不能为空
//                                    goodsPrice.set(sizeAndRolor.getGoodsPrice());//获取一级默认价格
//                                }
//                            }
//
//                        ToolUtil.copyProperties(srv,sizeVO1);
//                        return sizeVO1;
//                    }).collect(Collectors.toList());
//
//                sr.setSizeList(sizeVOList);//尺寸的集合
//                sr.setInventory(num);//库存数量
//                sr.setGoodsPrice(goodsPrice);//规格和尺寸价格
//
//                ToolUtil.copyProperties(sizeAndRolor,sr);
//                return sr;
//            }).collect(Collectors.toList());
//
//         return sizeAndRolorList;
//      }


//    public SizeAndRolorVO SizeAndRolorList(String goodsId,String colorId,String sizeId) {
//
//           List<SizeAndRolor> sizeAndRolorList = this.getSizeAndRolorList(goodsId,colorId);
//
//            SizeAndRolor data = new SizeAndRolor();
//            //获取默认规格第一条数据
//           if(ToolUtil.isNotEmpty(sizeAndRolorList)){
//               data = sizeAndRolorList.get(0);
//           }else {
//               return null;
//           }
//
//            SizeAndRolorVO sizeAndRolor = new SizeAndRolorVO();
//
//            ToolUtil.copyProperties(data,sizeAndRolor);
//            //获取所有规格颜色
//            List<SizeAndRolor> list = this.getSizeAndRolorList(goodsId,null);
//            List<ColorVO>  colorList = list.parallelStream().map(color ->{
//                ColorVO colorVO = new ColorVO();
//                ToolUtil.copyProperties(color,colorVO);
//                return colorVO;
//            }).collect(Collectors.toList());
//            sizeAndRolor.setColor(colorList);//获取集合规格颜色
//
//
//            AtomicReference<Integer> num = new AtomicReference<>(0);
//            AtomicReference<BigDecimal> goodsPrice = new AtomicReference<>();
//            //获取当前颜色下的所有规格的库存数量
//           List<SizeAndRolor> sizeInventory = Optional.ofNullable(this.getInventory(goodsId,Optional.ofNullable(colorId).orElse(data.getId())))
//                   .orElse(null);
//            List<SizeVO>  sizeList = sizeInventory.parallelStream().map(size ->{
//                SizeVO sizeVO = new SizeVO();
//                if(ToolUtil.isNotEmpty(sizeId)){
//                    if(sizeId.equals(size.getId())){
//                        num.updateAndGet(v -> v + size.getInventory());//单个尺寸的库存数量
//                    }
//                    if(ToolUtil.isNotEmpty(size.getGoodsPrice())) { //判断规格价格不能为空
//                            goodsPrice.set(size.getGoodsPrice());//获取二级规格和尺寸价格
//                    }
//                }else {
//                    num.updateAndGet(v -> v + size.getInventory());//按每种颜色的总和库存数量
//                     if(ToolUtil.isNotEmpty(size.getGoodsPrice())){//判断规格价格不能为空
//                            goodsPrice.set(goodMapper.selectById(goodsId).getGoodNewPrice());//获取一级默认价格
//                     }
//                }
//                ToolUtil.copyProperties(size,sizeVO);
//                return sizeVO;
//             }).collect(Collectors.toList());
//            sizeAndRolor.setSizeList(sizeList);//尺寸大小
//            sizeAndRolor.setGoodsPrice(goodsPrice);//价格
//            sizeAndRolor.setInventory(num);//库存
//
//           return Optional.ofNullable(sizeAndRolor)
//                   .orElse(null);
//    }
//
//
//
//    public List<SizeAndRolor>  getSizeAndRolorList(String goodsId,String colorId) {
//        return this.list(new QueryWrapper<SizeAndRolor>().lambda()
//                .eq(SizeAndRolor::getGoodsId,goodsId)
//                .eq(ToolUtil.isNotEmpty(colorId),SizeAndRolor::getId,colorId)
//                .eq(SizeAndRolor::getIsParent,1));
//    }
//
//
//    public List<SizeAndRolor>  getInventory(String goodsId,String colorId) {
//        return this.list(new QueryWrapper<SizeAndRolor>().lambda()
//                .eq(SizeAndRolor::getGoodsId,goodsId)
//                .eq(ToolUtil.isNotEmpty(colorId),SizeAndRolor::getParentId,colorId)
//                .eq(SizeAndRolor::getIsParent,0));
//    }
//
//
//    @Override
//    public String getSizeName(String id) {
//
//        return Optional.ofNullable(this.getById(id))
//                .map(SizeAndRolor::getSize).orElse(null);
//    }
//
//    @Override
//    public String getColorName(String id) {
//
//        return Optional.ofNullable(this.getById(id))
//                .map(SizeAndRolor::getColor).orElse(null);
//    }
//
//    @Override
//    public Integer getInventory(String id) {
//
//        return Optional.ofNullable(this.getById(id))
//                .map(SizeAndRolor::getInventory).orElse(null);
//    }




}