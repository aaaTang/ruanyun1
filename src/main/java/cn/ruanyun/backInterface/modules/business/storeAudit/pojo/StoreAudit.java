package cn.ruanyun.backInterface.modules.business.storeAudit.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家入驻审核
 *
 * @author fei
 */
@Data
@Entity
@Table(name = "t_store_audit")
@TableName("t_store_audit")
public class StoreAudit extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商家类型")
    private StoreTypeEnum storeType;

    @ApiModelProperty("姓名或者店铺名称")
    private String username;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("服务类型, 四大金刚注意")
    private String classificationId;

    @ApiModelProperty("所在城市id")
    private String areaId;

    @ApiModelProperty("身份证正面")
    private String idCardFront;

    @ApiModelProperty("身份证反面")
    private String idCardBack;

    @ApiModelProperty("营业执照")
    private String businessCard;

    @ApiModelProperty("其他证件照")
    private String otherIdPhotos;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("维度")
    private String latitude;

    @ApiModelProperty("审核意见")
    private String checkAdvice;

    @ApiModelProperty("审核状态")
    private CheckEnum checkEnum = CheckEnum.PRE_CHECK;

    @ApiModelProperty("支付宝账号")
    private String alipayAccount;

    @ApiModelProperty("微信账号")
    private String wechatAccount;
}
