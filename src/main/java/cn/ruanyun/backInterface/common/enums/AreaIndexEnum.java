package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 城市索引
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-13 11:29
 **/
public enum AreaIndexEnum {

    A(1,"A"),
    B(2,"B"),
    C(3,"C"),
    D(4,"D"),
    E(5,"E"),
    F(6,"F"),
    G(7,"G"),
    H(8,"H"),
    I(9,"I");

    AreaIndexEnum(int code, String value) {
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

