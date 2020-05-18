package cn.ruanyun.backInterface.modules.business.order.DTO;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class OffLineOrderDto {

    /**
     * 销售员工id
     */
    private String staffId;


    /**
     * 商品描述
     */
    private String goodDesc;


    /**
     * 总价格
     */
    private BigDecimal totalPrice;


    /**
     * 支付类型
     */
    private PayTypeEnum payTypeEnum;
}
