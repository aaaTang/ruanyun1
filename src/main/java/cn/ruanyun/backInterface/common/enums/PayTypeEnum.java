package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 支付方式
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum PayTypeEnum {

    /**
     * 微信支付
     */
    WE_CHAT(1,"微信支付"),


    /**
     * 支付宝支付
     */
    ALI_PAY(2,"支付宝支付"),


    /**
     * 余额支付
     */
    BALANCE(3, "余额支付");


    PayTypeEnum(int code, String value) {
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

