package cn.ruanyun.backInterface.modules.business.itemAttrKey.serviceimpl;

import cn.ruanyun.backInterface.modules.business.itemAttrKey.VO.ItemAttrKeyVO;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.VO.WebItemAttrKeyVO;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.VO.ItemAttrValVO;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.VO.WebItemAttrValVO;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

           //查询规格列表
      List<ItemAttrKey> itemAttrKey = this.list(new QueryWrapper<ItemAttrKey>().lambda()
            .eq(ToolUtil.isNotEmpty(classId),ItemAttrKey::getClassId,classId));

      List<WebItemAttrKeyVO> itemAttrKeyVOList = new ArrayList<>();

        for (ItemAttrKey attrKey : itemAttrKey) {
            WebItemAttrKeyVO keyVO = new WebItemAttrKeyVO();
            keyVO.setId(attrKey.getId()).setTitle(attrKey.getAttrName());

            //查询规格的属性列表
            List<ItemAttrVal> itemAttrVal =
                    itemAttrValMapper.selectList(Wrappers.<ItemAttrVal>lambdaQuery().eq(ItemAttrVal::getAttrId,attrKey.getId()));

            List<WebItemAttrValVO> itemAttrValVOList = new ArrayList<>();

            for (ItemAttrVal attrVal : itemAttrVal) {
                WebItemAttrValVO attrValVO = new WebItemAttrValVO();
                attrValVO.setId(attrVal.getId()).setTitle(attrVal.getAttrValue());

                itemAttrValVOList.add(attrValVO);
            }

            keyVO.setChildren(itemAttrValVOList);
            itemAttrKeyVOList.add(keyVO);
        }

        return itemAttrKeyVOList;
    }


}