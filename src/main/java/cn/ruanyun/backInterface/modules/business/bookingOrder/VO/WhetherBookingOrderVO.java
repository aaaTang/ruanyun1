package cn.ruanyun.backInterface.modules.business.bookingOrder.VO;

import lombok.Data;

@Data
public class WhetherBookingOrderVO {

    /**
     * 预约表id
     */
    private String id;


    /**
     * 预约时间
     */
    private String bookingTime;


    /**
     * 同意预约 0等待  1同意  -1 拒绝
     */
    private Integer  consent;
}
