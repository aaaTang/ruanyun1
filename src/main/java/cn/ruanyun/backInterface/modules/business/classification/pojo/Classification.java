package cn.ruanyun.backInterface.modules.business.classification.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AreaIndexEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 分类管理
 * @author fei
 */
@Data
@Entity
@Table(name = "t_classification")
@TableName("t_classification")
public class Classification extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


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