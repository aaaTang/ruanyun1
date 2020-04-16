package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: xboot-plus
 * @description: 退货类别枚举
 * @author: fei
 * @create: 2020-02-13 14:45
 **/
public enum AfterSaleTypeEnum {


    /**
     * 退款
     */
    APPLY(1,"退款"),

    /**
     * 退货退款
     */
    NO_PASS(2,"退货退款");


     AfterSaleTypeEnum(int code, String value) {
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
