package cn.ruanyun.backInterface.modules.business.itemAttrKey.serviceimpl;

import cn.ruanyun.backInterface.modules.business.itemAttrKey.VO.ItemAttrKeyVO;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.VO.ItemAttrValVO;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.ArrayMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 规格管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IItemAttrKeyServiceImpl extends ServiceImpl<ItemAttrKeyMapper, ItemAttrKey> implements IItemAttrKeyService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private ItemAttrValMapper itemAttrValMapper;


       @Override
       public void insertOrderUpdateItemAttrKey(ItemAttrKey itemAttrKey) {

           if (ToolUtil.isEmpty(itemAttrKey.getCreateBy())) {

                       itemAttrKey.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       itemAttrKey.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(itemAttrKey)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeItemAttrKey(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List getItemAttrKeyList(String classId) {
           //获取规格列表
           List<ItemAttrKey> itemAttrKey = this.list(new QueryWrapper<ItemAttrKey>().lambda()
                   .eq(ToolUtil.isNotEmpty(classId),ItemAttrKey::getClassId,classId));

           List<ItemAttrKeyVO> itemAttrKeyVO = itemAttrKey.parallelStream().map(itemAttrKey1 -> {
               //获取规格属性
               ItemAttrKeyVO AttrKey = new ItemAttrKeyVO();
                    List<ItemAttrVal> itemAttrVal = itemAttrValMapper.selectList(new QueryWrapper<ItemAttrVal>().lambda()
                    .eq(ItemAttrVal::getAttrId,itemAttrKey1.getId())
                    );
               ToolUtil.copyProperties(itemAttrKey1,AttrKey);

               List<ItemAttrValVO> itemAttrValVO = new ArrayList<>();

               for (ItemAttrVal attrValVO : itemAttrVal) {
                   ItemAttrValVO attrVal = new ItemAttrValVO();
                   attrVal.setId(attrValVO.getId()).setAttrValue(attrValVO.getAttrValue());
                   itemAttrValVO.add(attrVal);
               }
                AttrKey.setVal(itemAttrValVO);

               return AttrKey;
           }).collect(Collectors.toList());

        return itemAttrKeyVO;
    }


}