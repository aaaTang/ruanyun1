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
    I(9,"I"),
    J(10,"J"),
    K(11,"K"),
    L(12,"L"),
    M(13,"M"),
    N(14,"N"),
    O(15,"O"),
    P(16,"P"),
    Q(17,"Q"),
    R(18,"R"),
    S(19,"S"),
    T(20,"T"),
    U(21,"U"),
    V(22,"V"),
    W(23,"W"),
    X(24,"X"),
    Y(25,"Y"),
    Z(26,"Z");


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

