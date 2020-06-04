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
public enum ShopCartTypeEnum {

    /**
     * 商品
     */
    GOOD(1,"商品"),

    /**
     * 商品套餐
     */
    GOOD_PACKAGE(2,"商品套餐"),

    /**
     * 档期
     */
    AUCTION_CALENDAR(3,"档期");




    ShopCartTypeEnum(int code, String value) {
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

