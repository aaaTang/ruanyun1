package cn.ruanyun.backInterface.modules.business.classification.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BackAreaVO {

    private String id;
    /**
     * 分类名称
     */
    private String title;


    /**
     * 父id
     */
    private String parentId;


    /**
     * 图片
     */
    private String pic;


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
     * 是否启用 0启用 -1未启用
     */
    private Integer status = CommonConstant.STATUS_NORMAL;



}
