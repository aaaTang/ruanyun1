package cn.ruanyun.backInterface.modules.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-27 19:22
 **/

@Data
@Accessors(chain = true)
public class UserBalanceVo {


    @ApiModelProperty(value = "冻结余额")
    private BigDecimal freezeBalance;

    @ApiModelProperty(value = "正常余额")
    private BigDecimal normalBalance;
}
