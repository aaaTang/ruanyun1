package cn.ruanyun.backInterface.modules.business.area.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AreaIndexEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;


/**
 * 后台获取城市列表输出层
 */
@Data
@Accessors(chain = true)
public class BackAreaVO {


    private String id;

    /**
     * 城市名称
     */
    private String title;


    /**
     * 父id
     */
    private String parentId;


    /**
     * 城市索引
     */
    private AreaIndexEnum areaIndex;


    /**
     * 是否为父节点(含子节点) 默认false
     */
    private Boolean isParent = false;


    /**
     * 排序值
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;


    /**
     * 是否启用 0开放 -1未开放
     */
    private Integer status = CommonConstant.STATUS_NORMAL;

}
