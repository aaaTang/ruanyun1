package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class ShopDatelistVO {

    private String id;
    /**
     * 商家类型
     */
    private StoreTypeEnum storeType;


    /**
     * 用户姓名
     */
    private String username;


    /**
     * 手机
     */
    private String mobile;

    /**
     * 身份证正面
     */
    private String idCardFront;


    /**
     * 身份证反面
     */
    private String idCardBack;


    /**
     * 营业执照
     */
    private String businessCard;


    /**
     * 地址
     */
    private String address;
    /**
     * 店铺轮播图
     */
    @Column(length = 1000)
    private String  pic ;
    /**
     * 店铺名称
     */
    private String shopName;

}
