package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 入驻类型
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum StoreTypeEnum {

    /**
     * 个人商家
     */
    INDIVIDUALS_IN(1,"个人商家"),

    /**
     * 商家
     */
    MERCHANTS_SETTLED(2,"商家"),

    /**
     * 工作室个人商家
     */
    STUDIO_MERCHANTS(3, "工作室个人商家");

    StoreTypeEnum(int code, String value) {
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

