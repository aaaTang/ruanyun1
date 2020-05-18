package cn.ruanyun.backInterface.modules.business.staffManagement.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StaffListVo {


    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用戶昵称
     */
    private String nickName;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 个人简介
     */
    private String individualResume;

    /**
     * 当前销售额
     */
    private BigDecimal saleAmount = new BigDecimal(0);
}
