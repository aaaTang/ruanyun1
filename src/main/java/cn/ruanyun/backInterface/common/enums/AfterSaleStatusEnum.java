package cn.ruanyun.backInterface.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @program: xboot-plus
 * @description: 退货状态枚举
 * @author: fei
 * @create: 2020-02-13 14:45
 **/
public enum AfterSaleStatusEnum {


    /**
     * 申请
     */
    APPLY(1, "申请"),

    /**
     * 审核通过
     */
    APPLY_PASS(2, "审核通过"),

    /**
     * 购买者，发货填写快递单号
     */
    GOOD_DELIVER(3, "发货"),

    /**
     * 确认退款
     */
    FINISH(4, "确认退款"),

    /**
     * 货物有问题
     */
    REVOCATION(-3, "撤销"),

    /**
     * 货物有问题
     */
    GOOD_NO_PASS(-2, "货物不完整"),

    /**
     * 申请审核不通过
     */
    APPLY_NO_PASS(-1, "审核不通过");


     AfterSaleStatusEnum(int code, String value) {
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
