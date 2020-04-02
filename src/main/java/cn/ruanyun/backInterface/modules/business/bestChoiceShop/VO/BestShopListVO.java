package cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BestShopListVO {

    /**
     * 用户id
     */
    private String userid;

    /**
     * 严选商家表id
     */
    private String strictId;
    /**
     * 是否是严选商家，0否，1是
     */
    private Integer strict;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺图片
     */
    private String pic;
    /**
     * 店铺下商品最低价
     */
    private String price;

}
