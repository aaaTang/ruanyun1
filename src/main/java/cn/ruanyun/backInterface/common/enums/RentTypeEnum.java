package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 布尔类型
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum RentTypeEnum {


    /**
     * 暂未支付
     */
    NO_PAY(1, "暂未支付"),

    /**
     * 线上支付
     */
    ONLINE_PAY(2,"线上支付"),

    /**
     * 线下支付
     */
    OFFLINE_PAY(3,"线下支付");

    RentTypeEnum(int code, String value) {
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

