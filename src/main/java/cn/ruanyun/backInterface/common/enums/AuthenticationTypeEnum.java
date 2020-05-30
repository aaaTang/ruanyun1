package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Administrator
 * @author  连锁认证
 */

public enum AuthenticationTypeEnum {

    PRE_CHECK(0,"待确定"),

    MERCHANT(1,"品牌商家"),

    ALLIANCE(2,"品牌联盟");

    AuthenticationTypeEnum(int code, String value) {
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
