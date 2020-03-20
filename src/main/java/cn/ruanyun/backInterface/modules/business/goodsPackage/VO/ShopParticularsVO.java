package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import lombok.Data;

import javax.persistence.Column;

@Data
public class ShopParticularsVO {

    private String id;

    /**
     * 店铺名称
     */
    private String username;
    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    /**
     * 店铺评分
     */
    private String score;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;
}
