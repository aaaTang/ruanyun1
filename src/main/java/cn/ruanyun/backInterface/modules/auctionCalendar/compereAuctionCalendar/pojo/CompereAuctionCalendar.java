package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 主持人特殊档期价格管理接口
 * @author z
 */
@Data
@Entity
@Table(name = "t_compere_auction_calendar")
@TableName("t_compere_auction_calendar")
public class CompereAuctionCalendar extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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

}