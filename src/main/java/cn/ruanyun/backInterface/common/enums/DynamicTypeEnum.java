package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户动态类型
 * @program: ruanyun
 * @description:
 * @author: z
 * @create: 2020年4月13日13:42:01
 */
public enum DynamicTypeEnum {

    DYNAMIC(1,"动态"),
    VIDEO(2,"视频"),
    ASK(3,"问答");


    DynamicTypeEnum(int code, String value) {
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
