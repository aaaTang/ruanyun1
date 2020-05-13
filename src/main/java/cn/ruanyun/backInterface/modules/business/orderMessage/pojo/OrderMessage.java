package cn.ruanyun.backInterface.modules.business.orderMessage.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Year;
import java.util.Date;

/**
 * 订单消息
 * @author z
 */
@Data
@Entity
@Table(name = "t_order_message")
@TableName("t_order_message")
@Accessors(chain = true)
public class OrderMessage extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *商品名称
     */
    private String title;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单编码
     */
    private String orderNum;

    /**
     * 订单时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @ApiModelProperty(value = "状态 0默认未读 1已读 2回收站")
    private Integer status = CommonConstant.MESSAGE_STATUS_UNREAD;
}