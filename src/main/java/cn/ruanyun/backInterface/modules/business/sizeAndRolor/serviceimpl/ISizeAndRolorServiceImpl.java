package cn.ruanyun.backInterface.modules.business.sizeAndRolor.serviceimpl;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.VO.ItemAttrKeyVO;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.WebInventoryVO;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.itemListVO;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.itemVO;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
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

           SizeAndRolor size = super.getOne(new QueryWrapper<SizeAndRolor>().lambda()
                   .eq(SizeAndRolor::getAttrSymbolPath,sizeAndRolor.getAttrSymbolPath())
                   .eq(SizeAndRolor::getGoodsId,sizeAndRolor.getGoodsId())
                   .eq(SizeAndRolor::getCreateBy,securityUtil.getCurrUser().getId()));

           if (ToolUtil.isEmpty(size)) {
               sizeAndRolor.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               sizeAndRolor.setId(size.getId());
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

   /* @Override
    public Map<String,Object> SizeAndRolorList(String goodsId) {

        Map<String,Object> map = new HashMap<>();

        Good good = Optional.ofNullable(goodMapper.selectOne(new QueryWrapper<Good>().lambda().eq(Good::getId,goodsId).eq(Good::getTypeEnum, GoodTypeEnum.GOOD)))
              .orElse(null);
        if(ToolUtil.isNotEmpty(good.getGoodCategoryId())){
            //按分类获取规格
            List<ItemAttrKey> itemKey = itemAttrKeyMapper.selectList(
                    new QueryWrapper<ItemAttrKey>().lambda().eq(ItemAttrKey::getClassId,good.getGoodCategoryId()));

            List<ItemAttrKeyVO> itemAttrKeyVO = new ArrayList<>();
            for (ItemAttrKey itemAttrKey : itemKey) {
                ItemAttrKeyVO attrKey = new ItemAttrKeyVO();
                //按规格获取规格属性
                List<ItemAttrVal> itemAttrVal =  Optional.ofNullable(itemAttrValMapper.selectList(
                        new QueryWrapper<ItemAttrVal>().lambda().eq(ItemAttrVal::getAttrId,itemAttrKey.getId())))
                        .orElse(null);

                List<ItemAttrValVO> itemAttrValVO = new ArrayList<>();
                for (ItemAttrVal itemVal : itemAttrVal) {
                    ItemAttrValVO attrVal= new ItemAttrValVO();
                    attrVal.setId(itemVal.getId()).setAttrValue(itemVal.getAttrValue());
                    itemAttrValVO.add(attrVal);
                }
                attrKey.setId(itemAttrKey.getId()).setAttrName(itemAttrKey.getAttrName()).setVal(itemAttrValVO);
                itemAttrKeyVO.add(attrKey);
            }
            map.put("itemAttrKeyVO",itemAttrKeyVO);

            List<SizeAndRolor> list = sizeAndRolorMapper.selectList(
                    new QueryWrapper<SizeAndRolor>().lambda().eq(SizeAndRolor::getGoodsId,goodsId));
            Integer inventory = 0;
            for (SizeAndRolor s : list) {
                inventory+=s.getInventory();
            }
            map.put("goodsPrice",good.getGoodNewPrice());//商品价格
            map.put("pic",(list.size() >= 1 ? list.get(0).getPic() : ""));//商品图片
            map.put("inventory",inventory);//商品库存

            return  map;
        }else {
            return  null;
        }
    }*/

    /**
     * 获取商品规格和大小
     * @param goodsId  商品id
     * @param buyState 购买状态 1购买 2租赁
     * @return
     */
    @Override
    public Map<String,Object> SizeAndRolorList(String goodsId,Integer buyState) {

        Map<String,Object> map = new HashMap<>();
        //1.首先查商品的分类
        Good good = Optional.ofNullable(goodMapper.selectOne(new QueryWrapper<Good>().lambda().eq(Good::getId,goodsId).eq(Good::getTypeEnum, GoodTypeEnum.GOOD)))
                .orElse(null);

        //商品分类不为空
        if(ToolUtil.isNotEmpty(good.getGoodCategoryId())){

            //2.按分类获取规格
            List<ItemAttrKey> itemKey = itemAttrKeyMapper.selectList(
                    new QueryWrapper<ItemAttrKey>().lambda().eq(ItemAttrKey::getClassId,good.getGoodCategoryId()));

            //循环规格
            List<ItemAttrKeyVO> itemAttrKeyVO = new ArrayList<>();
            for (ItemAttrKey itemAttrKey : itemKey) {
                ItemAttrKeyVO attrKey = new ItemAttrKeyVO();
                //3.按规格获取规格属性
                List<ItemAttrVal> itemAttrVal =  Optional.ofNullable(itemAttrValMapper.selectList(
                        new QueryWrapper<ItemAttrVal>().lambda().eq(ItemAttrVal::getAttrId,itemAttrKey.getId())))
                        .orElse(null);

                List<ItemAttrValVo> itemAttrValVO = new ArrayList<>();
                //循环规格属性
                for (ItemAttrVal itemVal : itemAttrVal) {
                    ItemAttrValVo attrVal= new ItemAttrValVo();
                    attrVal.setId(itemVal.getId()).setAttrValue(itemVal.getAttrValue());
                    itemAttrValVO.add(attrVal);
                }
                attrKey.setId(itemAttrKey.getId()).setAttrName(itemAttrKey.getAttrName()).setVal(itemAttrValVO);
                itemAttrKeyVO.add(attrKey);
            }
            map.put("itemAttrKeyVO",itemAttrKeyVO);

            List<SizeAndRolor> list = sizeAndRolorMapper.selectList(
                    new QueryWrapper<SizeAndRolor>().lambda().eq(SizeAndRolor::getGoodsId,goodsId));
            Integer inventory = 0;
            for (SizeAndRolor s : list) {
                inventory+=s.getInventory();
            }
            map.put("goodsPrice",good.getGoodNewPrice());//商品价格
            map.put("pic",(list.size() >= 1 ? list.get(0).getPic() : ""));//商品图片
            map.put("inventory",inventory);//商品库存

            if(ToolUtil.isNotEmpty(list)){
                //商品现有的规格组合 给app做规格组合匹配
                map.put("itemList",this.getItemList(list));
            }

            return  map;
        }else {
            return  null;
        }
    }


    /**
     * 查询APP的现有规格属性组合
     * @param list
     * @return
     */
    public List getItemList(List<SizeAndRolor> list){

        List<itemListVO> itemList = new ArrayList<>();

        //循环这个商品的已有的规格
        for (SizeAndRolor sizeAndRolor : list) {

            itemListVO listVO = new itemListVO();

            List<itemVO> itemVO = new ArrayList<>();

            String[] id = sizeAndRolor.getAttrSymbolPath().split(",");

            for (String s : id) {
                itemVO item = new itemVO();

                item.setValId(s);
                item.setValName(Optional.ofNullable(itemAttrValMapper.selectById(s)).map(ItemAttrVal::getAttrValue).orElse("规格属性未找到！"));
                item.setKeyId(Optional.ofNullable(itemAttrKeyMapper.selectById(itemAttrValMapper.selectById(s).getAttrId())).map(ItemAttrKey::getId).orElse("规格未找到！"));
                item.setKeyName(Optional.ofNullable(itemAttrKeyMapper.selectById(itemAttrValMapper.selectById(s).getAttrId())).map(ItemAttrKey::getAttrName).orElse("规格未找到！"));
                itemVO.add(item);
            }

            itemList.add(listVO.setItemVO(itemVO));
        }

        return itemList;
    }




    @Override
    public Map<String,Object> getInventory(String ids,String goodsId) {

        Map<String,Object> map = new HashMap<>();
        SizeAndRolor s =  this.getOne(Wrappers.<SizeAndRolor>lambdaQuery().eq(SizeAndRolor::getAttrSymbolPath,ids).eq(SizeAndRolor::getGoodsId,goodsId));
           if(ToolUtil.isNotEmpty(s)){
               map.put("inventory",s.getInventory());
               map.put("goodsPrice",s.getGoodPrice());
               map.put("goodDeposit",s.getGoodDeposit());
               map.put("goodDalancePayment",s.getGoodDalancePayment());
               map.put("pic",s.getPic());
           }else {
               map.put("inventory",0);
               map.put("goodsPrice",0);
               map.put("goodDeposit",0);
               map.put("goodDalancePayment",0);
               map.put("pic",null);
           }
        return map;
    }


    /**
     * 获取配置信息
     *
     * @param attrSymbolPath
     * @return
     */
    @Override
    public SizeAndRolor getOneByAttrSymbolPath(String attrSymbolPath,String createBy,String goodId) {
        return Optional.ofNullable(this.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
            .eq(SizeAndRolor::getAttrSymbolPath,attrSymbolPath)
            .eq(SizeAndRolor::getCreateBy,createBy)
            .eq(SizeAndRolor::getGoodsId,goodId)
                )).orElse(null);
    }


    /**
     * WEB按商品获取库存
     * @return
     */
    @Override
    public List getWebInventory(String ids,String goodsId) {
        List<SizeAndRolor> sizeAndRolor = this.list(new QueryWrapper<SizeAndRolor>().lambda()
                .eq(ToolUtil.isNotEmpty(goodsId),SizeAndRolor::getGoodsId,goodsId).eq(SizeAndRolor::getDelFlag,0)
                .eq(ToolUtil.isNotEmpty(ids),SizeAndRolor::getId,ids)
        );

        List<WebInventoryVO> webInventoryVOS = sizeAndRolor.parallelStream().map(sizeAndRolor1 -> {
            WebInventoryVO webInventory = new WebInventoryVO();

            //获取规格属性id
            String[] attrVal = sizeAndRolor1.getAttrSymbolPath().split(",");
            List<ItemAttrVal> itemAttrVal = new ArrayList<>();
            for (String s : attrVal) {ItemAttrVal AttrVal  = itemAttrValMapper.selectById(s);itemAttrVal.add(AttrVal);}

            String attrName = "";//规格名称拼接
            for(int i= 0;i<itemAttrVal.size();i++){attrName+=itemAttrVal.get(i).getAttrValue();
                if(itemAttrVal.size()-1>i){attrName+=",";}
            }
            webInventory.setAttrSymbolPathName(attrName);

            ToolUtil.copyProperties(sizeAndRolor1,webInventory);
            return webInventory;
        }).collect(Collectors.toList());

        return webInventoryVOS;
    }


    @Override
    public String attrSymbolPathName(String id) {
        String[] attrVal = id.split(",");

        List<ItemAttrVal> itemAttrVal = new ArrayList<>();
        for (String s : attrVal) {ItemAttrVal AttrVal  = itemAttrValMapper.selectById(s);itemAttrVal.add(AttrVal);}

        String attrName = "";//规格名称拼接
        for(int i= 0;i<itemAttrVal.size();i++){attrName+=itemAttrVal.get(i).getAttrValue();
            if(itemAttrVal.size()-1>i){attrName+=",";}
        }
        return attrName;
    }

}