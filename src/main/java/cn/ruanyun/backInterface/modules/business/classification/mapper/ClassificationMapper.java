package cn.ruanyun.backInterface.modules.business.classification.mapper;

import cn.ruanyun.backInterface.modules.business.classification.VO.AppCategoryVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.BackAreaListVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.BackAreaVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;

import java.util.List;
import java.util.Map;

/**
 * 分类管理数据处理层
 * @author fei
 */
public interface ClassificationMapper extends BaseMapper<Classification> {

    /**
     * APP按一级分类ID查询二级分类
     */
    List<AppCategoryVO>  getAppCategoryList(String id);

    /**
     * 后端查询一级及二级
     */
    List<BackAreaVO>  getCategoryList(String id);

}