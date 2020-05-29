package cn.ruanyun.backInterface.modules.business.commonParam.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-27 19:09
 **/

@Data
@Accessors(chain = true)
public class CommonParamVo {

    private String id;

    /**
     * 订单冻结时间
     */
    private Integer freezeOrderTime;

    /**
     * 自动确认收货时间
     */
    private Integer autoReceiveOrderTime;

    /**
     * 自动评价时间
     */
    private Integer autoCommentOrderTime;

}
