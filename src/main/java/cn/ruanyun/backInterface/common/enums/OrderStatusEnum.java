package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: xboot-plus
 * @description: 订单状态枚举
 * @author: fei
 * @create: 2020-02-13 14:45
 **/
public enum OrderStatusEnum {


    /**
     * 待付款
     */
    PRE_PAY(1,"待付款"),

    /**
     * 已付款 app 展示待收货  后台显示待发货
     */
    PRE_SEND(2,"待发货"),

    /**
     * app 展示待收货  后台显示待评价
     */
    DELIVER_SEND(3,"待收货"),

    /**
     * APP租赁 用户待支付尾款      后台等待用户支付尾款
     */
    SETTLE_ACCOUNTS(9,"待付尾款"),

    /**
     * APP等待商家同意收款      后台确定收款
     */
    SETTLE_RECEIPT(10,"待收款"),
    /**
     * 待评价
     */
    PRE_COMMENT(4,"待评价"),

    /**
     * 退款售后
     */
    SALE_AFTER(5,"退款售后"),

    /**
     * 取消订单
     */
    CANCEL_ORDER(6,"取消订单"),

    /**
     * 完成订单
     */
    IS_COMPLETE(7,"完成订单"),

    /**
     * 退款完成
     */
    RETURN_FINISH(8,"退款完成");



     OrderStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    @EnumValue
    private final  int code;
    private  String value;

}
