package cn.ruanyun.backInterface.modules.fadada.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class PersonVerifyDto {

    @ApiModelProperty("实名认证套餐类型 0:三要素标准方案； 1:三要素补充方案； 2:四要素标准方案； 3:四要素补充方案； 4:纯三要素方案； 5:纯四要素方案；")
    private String verifiedWay;

    @ApiModelProperty("是否允许用户页面修改 1 允许， 2 不允许")
    private String pageModify;

    @ApiModelProperty(value = "回调地址")
    private String notifyUrl;

    @ApiModelProperty("同步通知 url")
    private String returnUrl;

    @ApiModelProperty("姓名")
    private String customerName;

    @ApiModelProperty("/是否支持其他证件类型, 身份证-0 其他 1")
    private String customerIdentType;

    @ApiModelProperty("证件号码")
    private String customerIdentNo;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "证件正面照下载地址")
    private String identFrontPath;

    @ApiModelProperty(value = "证件反面照下载地址")
    private String identBackPath;

    @ApiModelProperty("参数值为“1”：直接跳转到 return_url 或 法 大 大 指 定 页 面，参数值为“2”：需要用户点 击确认后跳转到 return_url 或 法大大指定页面")
    private String resultType;

    @ApiModelProperty("是否认证成功后自动申请实 名证书 参数值为“0”：不申请， 参数值为“1”：自动申请")
    private String certFlag;
}
