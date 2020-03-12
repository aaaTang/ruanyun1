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

    REDACT_DETAIL(1,"编辑详情页"),
    H5_WEB(2,"H5网页链接"),
    ACTIVITY_PAGE(3,"活动页面"),
    STORE_HOME(4,"商家店铺首页");

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

