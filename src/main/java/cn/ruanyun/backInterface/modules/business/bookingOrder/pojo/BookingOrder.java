package cn.ruanyun.backInterface.modules.business.bookingOrder.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 预约订单
 * @author fei
 */
@Data
@Entity
@Table(name = "t_booking_order")
@TableName("t_booking_order")
public class BookingOrder extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private String storeId;


    /**
     * 预约时间
     */
    private String bookingTime;


    /**
     * 同意预约 0等待  1同意  -1 拒绝
     */
    private Integer  consent  =  CommonConstant.STATUS_NORMAL;

}