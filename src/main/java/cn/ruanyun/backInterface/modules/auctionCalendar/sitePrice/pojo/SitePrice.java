package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 设置场地档期价格
 * @author z
 */
@Data
@Entity
@Table(name = "t_site_price")
@TableName("t_site_price")
public class SitePrice extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "场地价格")
    private BigDecimal sitePrice;
}