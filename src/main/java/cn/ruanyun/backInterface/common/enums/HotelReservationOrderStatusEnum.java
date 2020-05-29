package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: xboot-plus
 * @description: 退货状态枚举
 * @author: fei
 * @create: 2020-02-13 14:45
 **/
public enum HotelReservationOrderStatusEnum {


    /**
     * 待付款
     */
    PRE_PAY(1, "待付款"),

    /**
     * 待使用
     */
    PRE_USE(2, "待使用"),

    /**
     * 已完成
     */
    COMPLETE(3, "已完成"),

    /**
     * 取消
     */
    CANCEL(4, "取消");

     HotelReservationOrderStatusEnum(int code, String value) {
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
