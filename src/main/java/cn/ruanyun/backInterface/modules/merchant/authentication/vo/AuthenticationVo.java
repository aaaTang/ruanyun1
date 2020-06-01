package cn.ruanyun.backInterface.modules.merchant.authentication.vo;

import cn.ruanyun.backInterface.common.enums.AuthenticationTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class AuthenticationVo {

    private  String  id;
    /**
     * 商家名称
     */
    private  String  shopName;
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

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

}
