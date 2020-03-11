package cn.ruanyun.backInterface.modules.base.mapper;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.pojo.DictData;

import java.util.List;

/**
 * 字典数据数据处理层
 * @author fei
 */
public interface DictDataDao extends RuanyunBaseDao<DictData,String> {


    /**
     * 通过dictId和状态获取
     * @param dictId
     * @param status
     * @return
     */
    List<DictData> findByDictIdAndStatusOrderBySortOrder(String dictId, Integer status);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}