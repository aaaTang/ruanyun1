package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class SitePriceVO {

    private String id;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "场地价格")
    private String sitePrice;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
