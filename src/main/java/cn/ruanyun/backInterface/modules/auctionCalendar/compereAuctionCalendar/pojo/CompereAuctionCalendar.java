package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 主持人没有档期的时间
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

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;
}