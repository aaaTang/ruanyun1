package cn.ruanyun.backInterface.modules.merchant.trustIdentity.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;
}
