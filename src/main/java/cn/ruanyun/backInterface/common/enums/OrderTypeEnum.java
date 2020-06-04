package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * 订单类型枚举
 * @author root
 */
public enum OrderTypeEnum {

    /**
     * 套餐订单
     */
    GOODS_PACKAGE (1,"套餐订单"),

    /**
     * 商品订单
     */
    GOOD(2,"商品订单"),

    /**
     * 线下订单
     */
    OFFLINE_ORDER(3, "线下订单"),

    /**
     * 婚宴档期预约订单
     */
    SCHEDULE_ORDER(4, "婚宴档期预约订单"),

    /**
     * 主持人档期预约订单
     */
    COMPERE_ORDER(5, "主持人档期预约订单");



    OrderTypeEnum(int code, String value) {
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
