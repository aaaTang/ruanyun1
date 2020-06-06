package cn.ruanyun.backInterface.modules.business.storeAudit.vo;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 数据审核输出层
 */
@Data
@Accessors(chain = true)
public class StoreAuditVo {

    private String id;

    @ApiModelProperty("商家类型")
    private StoreTypeEnum storeType;

    @ApiModelProperty("姓名或者店铺名称")
    private String username;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("服务类型, 四大金刚注意")
    private String classificationId;

    @ApiModelProperty("服务类型")
    private String classificationName;

    @ApiModelProperty("所在城市名称")
    private String areaName;

    @ApiModelProperty("所在城市id")
    private String areaId;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("维度")
    private String latitude;

    @ApiModelProperty("身份证正面")
    private String idCardFront;

    @ApiModelProperty("身份证反面")
    private String idCardBack;

    @ApiModelProperty("支付宝账号")
    private String alipayAccount;

    @ApiModelProperty("微信账号")
    private String wechatAccount;

    @ApiModelProperty("营业执照")
    private String businessCard;

    @ApiModelProperty("审核状态")
    private CheckEnum checkEnum;

    @ApiModelProperty("审核意见")
    private String checkAdvice;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
