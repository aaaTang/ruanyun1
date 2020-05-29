package cn.ruanyun.backInterface.modules.business.commonParam.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公众参数
 * @author z
 */
@Data
@Entity
@Table(name = "t_common_param")
@TableName("t_common_param")
public class commonParam extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单冻结时间
     */
    private Integer freezeOrderTime = 1;

    /**
     * 自动确认收货时间
     */
    private Integer autoReceiveOrderTime = 1;

    /**
     * 自动评价时间
     */
    private Integer autoCommentOrderTime = 1;
}