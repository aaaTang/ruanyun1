package cn.ruanyun.backInterface.modules.business.withdrawDeposit.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 提现管理
 * @author z
 */
@Data
@Entity
@Table(name = "t_withdraw_deposit")
@TableName("t_withdraw_deposit")
public class WithdrawDeposit extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    //提现人 提现人手机号 提现金额 提现类型 提现账号 提现状态 创建时间

    /**
     * 提现人
     */
    private String withdrawUser;

    /**
     * 提现人手机号
     */
    private String mobile;

    /**
     * 提现金额
     */
    private BigDecimal money;

    /**
     * 提现类型
     */
    private PayTypeEnum payTypeEnum;

    /**
     * 提现账号
     */
    private String withdrawNumber;

    /**
     * 是否同意体检审核  -1等待 0.不同意 1.同意
     */
    private Integer type  =  CommonConstant.AWAIT;

    /**
     * 审核意见！
     */
    private String content;

}