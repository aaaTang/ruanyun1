package cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家优质服务
 * @author fei
 */
@Data
@Entity
@Table(name = "t_store_first_rate_service")
@TableName("t_store_first_rate_service")
public class StoreFirstRateService extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优质服务Ids")
    private String firstRateServiceIds;

    @ApiModelProperty("审核状态")
    private CheckEnum checkStatus = CheckEnum.PRE_CHECK;

    @ApiModelProperty("审核原因")
    private String checkReason;
}