package cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家信任标识
 * @author z
 */
@Data
@Entity
@Table(name = "t_trust_identity")
@TableName("t_trust_identity")
public class TrustIdentity extends RuanyunBaseEntity {

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
     * 状态
     */
    private CheckEnum status = CheckEnum.PRE_CHECK;

    /**
     * 审核原因
     */
    private String statusCause;
}