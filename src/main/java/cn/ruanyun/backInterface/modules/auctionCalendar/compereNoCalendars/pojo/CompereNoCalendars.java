package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设置主持人没有档期的时间
 * @author z
 */
@Data
@Entity
@Table(name = "t_compere_no_calendars")
@TableName("t_compere_no_calendars")
public class CompereNoCalendars extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品id")
    private String  goodsId;

    @ApiModelProperty("主持人 套餐 ，商品")
    private GoodTypeEnum goodTypeEnum;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;

}