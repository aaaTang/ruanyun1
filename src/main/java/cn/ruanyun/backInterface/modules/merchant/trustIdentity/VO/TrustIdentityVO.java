package cn.ruanyun.backInterface.modules.merchant.trustIdentity.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import lombok.Data;

@Data
public class TrustIdentityVO {

    private String id;

    /**
     * 店铺名称
     */
    private String  shopName;
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
