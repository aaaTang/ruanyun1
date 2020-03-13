package cn.ruanyun.backInterface.modules.business.area.VO;

import cn.ruanyun.backInterface.common.enums.AreaIndexEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: ruanyun
 * @description: app分类列表
 * @author: fei
 **/
@Data
@Accessors(chain = true)
public class AppAreaListVO {


    /**
     * 城市索引
     */
    private AreaIndexEnum areaIndex;

    /**
     * 子集城市分类列表
     */
    private List<AppAreaVO> areaListVOS;
}
