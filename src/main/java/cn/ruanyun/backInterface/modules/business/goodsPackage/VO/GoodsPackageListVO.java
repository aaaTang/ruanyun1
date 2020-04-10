package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

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
    @Column(length = 1000)
    private String pics;

    /**
     * 新价格
     */
    private String newPrice;

    /**
     * 旧价格
     */
    private String oldPrice;

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
