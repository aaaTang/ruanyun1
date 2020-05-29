package cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 档期订单
 * @author fei
 */
@Data
@Entity
@Table(name = "t_auction_calendar_order")
@TableName("t_auction_calendar_order")
public class AuctionCalendarOrder extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


}