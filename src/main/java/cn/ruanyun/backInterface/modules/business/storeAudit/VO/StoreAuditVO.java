package cn.ruanyun.backInterface.modules.business.storeAudit.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 数据审核输出层
 */
@Data
@Accessors(chain = true)
public class StoreAuditVO {

    /**
     * 商家类型
     */
    private StoreTypeEnum storeType;


    /**
     * 姓名或者店铺名称
     */
    private String username;


    /**
     * 手机
     */
    private String mobile;


    /**
     * 服务类型
     */
    private String classificationName;


    /**
     * 所在城市id
     */
    private String areaName;


    /**
     * 身份证正面
     */
    private String idCardFront;


    /**
     * 身份证反面
     */
    private String idCardBack;


    /**
     * 营业执照
     */
    private String businessCard;


    /**
     * 审核状态
     */
    private CheckEnum checkEnum = CheckEnum.PRE_CHECK;

}
