package cn.ruanyun.backInterface.modules.business.bookingOrder.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import lombok.Data;

import javax.persistence.Column;

@Data
public class BookingOrderVO {


    private String id;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 分类名称
     */
    private String title;

    /**
     *评论数量
     */
    private Integer commentNum;

    /**
     * 店铺评分
     */
    private Integer score;
}
