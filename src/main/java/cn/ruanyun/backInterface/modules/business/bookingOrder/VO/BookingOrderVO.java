package cn.ruanyun.backInterface.modules.business.bookingOrder.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class BookingOrderVO {

    /**
     * 店铺id
     */
    private String id;

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
     * 分类id
     */
    private String classId;

    /**
     *评论数量
     */
    private Integer commentNum;

    /**
     * 店铺评分
     */
    private Double score;

    /**
     * 预约状态
     */
    private String timeDetail;

}
