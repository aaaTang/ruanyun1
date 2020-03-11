package cn.ruanyun.backInterface.modules.base.service;



import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.base.entity.Dict;

import java.util.List;

/**
 * 字典接口
 * @author fei
 */
public interface DictService extends RuanyunBaseService<Dict,String> {

    /**
     * 排序获取全部
     * @return
     */
    List<Dict> findAllOrderBySortOrder();

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    List<Dict> findByTitleOrTypeLike(String key);
}