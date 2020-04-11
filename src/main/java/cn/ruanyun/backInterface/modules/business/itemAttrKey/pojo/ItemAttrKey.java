package cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 规格管理
 * @author z
 */
@Data
@Entity
@Table(name = "t_item_attr_key")
@TableName("t_item_attr_key")
public class ItemAttrKey extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品属性名称
     */
    private String attrName;




}