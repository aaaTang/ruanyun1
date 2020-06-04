package cn.ruanyun.backInterface.modules.fadada.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class CompanyVerifyDto {


    @ApiModelProperty("实名认证套餐类型")
    private String verifiedWay = "0";

    @ApiModelProperty("管理员认证套餐类型")
    private String mVerifiedWay;

    @ApiModelProperty("是否允许用户页面修改 1 允许， 2 不允许")
    private String pageModify;

    @ApiModelProperty("1.法人，2 代理人")
    private String companyPrincipalType;

    @ApiModelProperty(value = "回调地址")
    private String notifyUrl;

    @ApiModelProperty("同步通知 url")
    private String returnUrl;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("统一社会信用代码")
    private String creditNo;

    @ApiModelProperty("统一社会信用代码证件照路径")
    private String creditImagePath;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行帐号")
    private String bankId;

    @ApiModelProperty("开户支行名称")
    private String subbranchName;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人证件号")
    private String legalId;

    @ApiModelProperty("法人手机号(仅支持国内运营 商)")
    private String legalMobile;

    @ApiModelProperty("法人证件正面照下载地址")
    private String legalIdFrontPath;

    @ApiModelProperty("代理人姓名")
    private String agentName;

    @ApiModelProperty("代理人证件号")
    private String agentId;

    @ApiModelProperty("代理人手机号")
    private String agentMobile;

    @ApiModelProperty("代理人证件正面照下载地址")
    private String agentIdFrontPath;

    @ApiModelProperty("参数值为“1”：直接跳转到 return_url 或 法 大 大 指 定 页 面，参数值为“2”：需要用户点 击确认后跳转到 return_url 或 法大大指定页面")
    private String resultType;

    @ApiModelProperty("是否认证成功后自动申请实 名证书 参数值为“0”：不申请， 参数值为“1”：自动申请")
    private String certFlag;

}
