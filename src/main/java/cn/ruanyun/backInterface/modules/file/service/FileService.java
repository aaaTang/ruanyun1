package cn.ruanyun.backInterface.modules.file.service;


import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.file.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 文件管理接口
 * @author fei
 */
public interface FileService extends RuanyunBaseService<File,String> {

    /**
     * 多条件获取列表
     * @param file
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<File> findByCondition(File file, SearchVo searchVo, Pageable pageable);
}