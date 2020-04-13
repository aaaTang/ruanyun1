package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:11
 **/
@Data
@Accessors(chain = true)
public class OrderDetailVO extends OrderListVO {

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
