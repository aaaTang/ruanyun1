package cn.ruanyun.backInterface.modules.fadada.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 个人认证
 * @author z
 */
@Data
@Entity
@Table(name = "t_personal_certificate")
@TableName("t_personal_certificate")
@Accessors(chain = true)
public class PersonalCertificate extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /*--------------公共参数--------------------*/

    @ApiModelProperty(value = "实名认证套餐类型 0:三要素标准方案； 1:三要素补充方案； 2:四要素标准方案； 3:四要素补充方案； 4:纯三要素方案； 5:纯四要素方案；", hidden = true)
    private String verifiedWay = "1";

    @ApiModelProperty(value = "是否允许用户页面修改 1 允许， 2 不允许", hidden = true)
    private String pageModify = "1";

    @ApiModelProperty(value = "/是否支持其他证件类型, 身份证-0 其他 1", hidden = true)
    private String customerIdentType = "0";

    @ApiModelProperty(value = "参数值为“1”：直接跳转到 return_url 或 法 大 大 指 定 页 面，参数值为“2”：需要用户点 击确认后跳转到 return_url 或 法大大指定页面", hidden = true)
    private String resultType = "1";

    @ApiModelProperty(value = "是否认证成功后自动申请实 名证书 参数值为“0”：不申请， 参数值为“1”：自动申请", hidden = true)
    private String certFlag = "1";

    @ApiModelProperty(value = "回调地址", hidden = true)
    private String notifyUrl;

    @ApiModelProperty(value = "同步通知 url", hidden = true)
    private String returnUrl;




    /*-----------传参------------*/

    @ApiModelProperty("姓名")
    private String customerName;

    @ApiModelProperty("证件号码")
    private String customerIdentNo;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "证件正面照下载地址")
    private String identFrontPath;

    @ApiModelProperty(value = "证件反面照下载地址")
    private String identBackPath;


    /*-------------返回参数----------------*/

    @ApiModelProperty(value = "交易号", hidden = true)
    private String transactionNo;

    @ApiModelProperty(value = "实名认证地址", hidden = true)
    private String verifyUrl;

    @ApiModelProperty(value = "认证类型 1:查询个人 2.查询企业", hidden = true)
    private Integer authenticationType;
}