package cn.ruanyun.backInterface.modules.business.bookingOrder.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BackBookingOrderListVO {


    private String id;

    /**
     * 用户id
     */
    private String createBy;

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 商家id
     */
    private String storeId;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 预约时间
     */
    private String bookingTime;

    /**
     * 同意预约 0等待  1同意  -1 拒绝
     */
    private Integer  consent  =  CommonConstant.STATUS_NORMAL;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
