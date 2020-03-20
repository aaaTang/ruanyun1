package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoodsPackageListVO {

    private String id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 套餐图片
     */
    private String pics;

    /**
     * 新价格
     */
    private String newPrice;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户图片
     */
    private String userPic;
}
