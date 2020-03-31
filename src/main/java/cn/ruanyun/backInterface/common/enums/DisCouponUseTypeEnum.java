package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 优惠券类型
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum DisCouponUseTypeEnum {

    /**
     * 待使用
     */
    PRE_USE(1,"待使用"),

    /**
     * 已使用
     */
    HAVE_USE(2,"已使用"),

    /**
     * 已失效
     */
    HAVE_VALID(3,"已失效");

    DisCouponUseTypeEnum(int code, String value) {
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

