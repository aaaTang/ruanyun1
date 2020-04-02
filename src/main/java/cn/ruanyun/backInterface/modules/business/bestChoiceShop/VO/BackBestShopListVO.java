package cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class BackBestShopListVO {

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
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 地址
     */
    private String address;
    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;
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
