package cn.ruanyun.backInterface.modules.business.userRelationship.VO;


import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserOrderListVO {

    private String id;

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payTypeEnum;

    @ApiModelProperty(value = "总价格")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String pic;
}
