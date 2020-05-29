package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 场地档期
 * @author fei
 */
@Data
@Entity
@Table(name = "t_site_auction_calendar")
@TableName("t_site_auction_calendar")
public class SiteAuctionCalendar extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;

}