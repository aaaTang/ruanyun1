package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class PcGetCompereAuctionCalendarVO {

    private String  id;

    @ApiModelProperty("商品id")
    private String  goodsId;

    @ApiModelProperty("主持人 套餐 ，商品")
    private GoodTypeEnum goodTypeEnum;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "价格")
    private BigDecimal goodsPrice;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(" 0正常 1删除")
    private Integer delFlag;
}
