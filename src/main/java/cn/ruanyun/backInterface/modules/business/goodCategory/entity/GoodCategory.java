package cn.ruanyun.backInterface.modules.business.goodCategory.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_good_category")
@TableName("t_good_category")
@ApiModel(value = "商品分类")
public class GoodCategory extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "分类图片")
    private String pic;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;



}