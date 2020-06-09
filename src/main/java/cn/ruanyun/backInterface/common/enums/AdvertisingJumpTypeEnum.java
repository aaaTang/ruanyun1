package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum AdvertisingJumpTypeEnum {

    /**
     * 商品套餐详情页
     */
    GOOD_PACKAGE_PAGE(1,"套餐详情页"),

    /**
     * 商品详情页
     */
    GOOD_PAGE(2, "商品详情页"),

    /**
     * 商家店铺首页
     */
    STORE_HOME(4,"商家店铺首页"),

    /**
     * H5网页链接
     */
    H5_WEB(2,"H5网页链接");


    AdvertisingJumpTypeEnum(int code, String value) {
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

