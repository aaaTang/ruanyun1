package cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 规格属性管理
 * @author z
 */
@Data
@Entity
@Table(name = "t_item_attr_val")
@TableName("t_item_attr_val")
public class ItemAttrVal extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 规格id
     */
    private String attrId;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 规格属性名称
     */
    private String attrValue;
}