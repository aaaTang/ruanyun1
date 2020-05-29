package cn.ruanyun.backInterface.modules.business.itemAttrVal.serviceimpl;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
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


/**
 * 规格属性管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IItemAttrValServiceImpl extends ServiceImpl<ItemAttrValMapper, ItemAttrVal> implements IItemAttrValService {


    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void insertOrderUpdateItemAttrVal(ItemAttrVal itemAttrVal) {

        if (ToolUtil.isEmpty(itemAttrVal.getCreateBy())) {
            itemAttrVal.setCreateBy(securityUtil.getCurrUser().getId());
        } else {
            itemAttrVal.setUpdateBy(securityUtil.getCurrUser().getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(itemAttrVal)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeItemAttrVal(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }


    /**
     * 通过属性值，获取属性的名字
     * @param ids ids
     * @return return
     */
    @Override
    public List<String> getItemAttrVals(String ids) {
        return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(ids)))).map(itemAttrVals -> {
            return itemAttrVals.parallelStream().map(ItemAttrVal::getAttrValue).collect(Collectors.toList());
        }).orElse(null);
    }

    @Override
    public List<ItemAttrValVo> getItemAttrValVo(String ids) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(ids))))
                .map(itemAttrVals -> itemAttrVals.parallelStream().flatMap(itemAttrVal -> {

                    ItemAttrValVo itemAttrValVo = new ItemAttrValVo();
                    ToolUtil.copyProperties(itemAttrVal, itemAttrValVo);
                    return Stream.of(itemAttrValVo);
                }).collect(Collectors.toList()))
                .orElse(null);
    }


    /**
     * 获取规格属性值列表
     * @return ItemAttrVal
     */
    @Override
    public List<ItemAttrVal> getItemAttrValList(String keyId) {
        return Optional.ofNullable(this.list(new QueryWrapper<ItemAttrVal>().lambda()
                .eq(ToolUtil.isNotEmpty(keyId),ItemAttrVal::getAttrId,keyId)
        )).orElse(null);
    }


}