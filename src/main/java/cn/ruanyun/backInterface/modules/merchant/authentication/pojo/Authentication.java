package cn.ruanyun.backInterface.modules.merchant.authentication.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AuthenticationTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家连锁认证
 * @author z
 */
@Data
@Entity
@Table(name = "t_authentication")
@TableName("t_authentication")
public class Authentication extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 描述
     */
    private  String  title;

    /**
     * 图片
     */
    private  String  pic;

    /**
     * 附件
     */
    private  String  accessory;

    /**
     * 连锁认证：品牌商家、品牌联盟
     */
    private AuthenticationTypeEnum authenticationTypeEnum = AuthenticationTypeEnum.PRE_CHECK;

    /**
     * 状态
     */
    private CheckEnum status = CheckEnum.PRE_CHECK;

    /**
     * 审核原因
     */
    private String statusCause;
}