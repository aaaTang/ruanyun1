package cn.ruanyun.backInterface.modules.business.area.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AreaIndexEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 城市管理
 * @author fei
 */
@Data
@Entity
@Table(name = "t_area")
@TableName("t_area")
public class Area extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


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