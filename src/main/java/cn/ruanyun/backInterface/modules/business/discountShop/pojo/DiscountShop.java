package cn.ruanyun.backInterface.modules.business.discountShop.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 优惠券参加的商家
 * @author z
 */
@Data
@Entity
@Table(name = "t_discount_shop")
@TableName("t_discount_shop")
public class DiscountShop extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 优惠券id
     */
    private String discountId;

    /**
     * 商家id
     */
    private String shopId;
}