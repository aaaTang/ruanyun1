package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum CheckEnum {

    PRE_CHECK(1,"待审核"),
    CHECK_SUCCESS(2,"审核成功！"),
    CHECK_FAIL(3,"审核失败！");

    CheckEnum(int code, String value) {
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

