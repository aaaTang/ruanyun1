package cn.ruanyun.backInterface.modules.business.commodity.mapper;

import cn.ruanyun.backInterface.modules.business.commodity.VO.CommodityListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.commodity.pojo.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品管理数据处理层
 * @author zhu
 */
public interface CommodityMapper extends BaseMapper<Commodity> {

    List<CommodityListVO> CommodityList(@Param("obj")Commodity commodity);

}