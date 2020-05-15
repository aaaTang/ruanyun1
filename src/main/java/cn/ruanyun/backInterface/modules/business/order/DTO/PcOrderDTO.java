package cn.ruanyun.backInterface.modules.business.order.DTO;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import lombok.Data;

@Data
public class PcOrderDTO {


    private String id;

    /**
     * 用户类型
     */
    private String commonConstant;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 支付类型
     */
    private Integer payTypeEnum;

}
