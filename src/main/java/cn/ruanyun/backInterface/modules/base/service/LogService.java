package cn.ruanyun.backInterface.modules.base.service;



import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 * @author fei
 */
public interface LogService extends RuanyunBaseService<Log,String> {

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);

    /**
     * 删除所有
     */
    void deleteAll();
}
