package cn.ruanyun.backInterface.modules.business.discountShop.VO;

import lombok.Data;

@Data
public class DiscountShopListVO {

    /**
     * 优惠券id
     */
    private String discountId;

    /**
     * 商家id
     */
    private String shopId;

    /**
     * 商家名称
     */
    private String shopName;
}
