package cn.ruanyun.backInterface.modules.business.orderAfterSale.dto;

import cn.ruanyun.backInterface.common.enums.AfterSaleTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-27 20:15
 **/

@Data
@Accessors(chain = true)
public class OrderAfterSaleDto {


    private String id;

    private String orderId;

    /**
     * 实际退款金额
     */
    private BigDecimal actualRefundMoney;

}
