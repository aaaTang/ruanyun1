package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum PushTypeEnum {

    /**
     * h5页面
     */
    H5(1,"h5页面"),

    /**
     * 商家详情
     */
    STORE_DETAIL(2,"商家详情"),

    /**
     * 商品详情
     */
    GOOD_DETAIL(3,"商品详情"),

    /**
     * 套餐详情
     */
    SET_MEAL(4, "套餐详情"),

    /**
     * 作品
     */
    PRODUCTION(5, "作品");




    PushTypeEnum(int code, String value) {
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

