package cn.ruanyun.backInterface.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UsableRangeTypeEnum {


     ALL(0,"无限制"),

     CLASSIFY(1,"分类"),

     LABEL(2,"标签属性");


    UsableRangeTypeEnum(int code, String value) {
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
