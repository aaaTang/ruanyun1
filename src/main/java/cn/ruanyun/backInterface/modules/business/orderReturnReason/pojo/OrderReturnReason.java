package cn.ruanyun.backInterface.modules.business.orderReturnReason.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 退货原因
 * @author wj
 */
@Data
@Entity
@Table(name = "t_order_return_reason")
@TableName("t_order_return_reason")
public class OrderReturnReason extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退货原因
     */
    private String reason;


}