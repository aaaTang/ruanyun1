package cn.ruanyun.backInterface.common.enums;

import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * 订单类型枚举
 */
public enum OrderTypeEnum {


    /**
     * 预约
     */
    APPOINTMENT (1,"预约"),

    /**
     * 订单
     */
    ORDER(2,"订单"),

    /**
     * 套餐订单
     */
    GOODSPACKAGE (1,"套餐订单"),

    /**
     * 商品订单
     */
    GOOD(2,"商品订单"),

    /**
     * 线下订单
     */
    OFFLINE_ORDER(3, "线下订单");

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
