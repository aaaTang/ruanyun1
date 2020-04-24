package cn.ruanyun.backInterface.modules.business.storeAudit.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 数据审核输出层
 */
@Data
@Accessors(chain = true)
public class StoreAuditVO {

    private String id;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
    private CheckEnum checkEnum;

    /**
     * 审核意见
     */
    private String checkAdvice;

}
