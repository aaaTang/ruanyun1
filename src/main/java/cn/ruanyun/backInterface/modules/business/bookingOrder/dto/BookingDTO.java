package cn.ruanyun.backInterface.modules.business.bookingOrder.dto;

import lombok.Data;

@Data
public class BookingDTO {

    private String id;

    /**
     * 同意预约 0等待  1同意  -1 拒绝
     */
    private Integer  consent ;
}
