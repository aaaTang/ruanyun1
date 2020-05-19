package cn.ruanyun.backInterface.modules.business.privateNumberAx.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单
 * @author fei
 */
@Data
@Entity
@Table(name = "t_Private_number_ax")
@TableName("t_Private_number_ax")
@Accessors(chain = true)
public class PrivateNumberAx extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private String storeId;

    /**
     * 虚拟号段id
     */
    private String privateNumberId;

    /**
     * 绑定ID，唯一标识一组绑定关系。成功响应时必定返回。请记录该ID用于后续接口调用。
     */
    private String subscriptionId;


    /**
     * 绑定关系允许的呼叫方向，取值含义参见请求参数。
     *
     * 成功响应时必定返回。
     */
    private Integer callDirection;

    /**
     * 绑定关系保持时间，单位为秒，0表示永不过期。
     *
     * 成功响应时必定返回。
     */
    private Integer duration;

    /**
     *
     * 允许单次通话进行的最长时间，通话时间从被叫接通的时刻开始计算，0表示系统不主动结束通话。
     *
     * 成功响应时必定返回。
     */
    private Integer maxDuration;

}