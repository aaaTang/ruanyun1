package cn.ruanyun.backInterface.modules.business.firstRateService.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 优质服务
 * @author fei
 */
@Data
@Entity
@Table(name = "t_first_rate_service")
@TableName("t_first_rate_service")
public class FirstRateService extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类id")
    private String goodCategoryId;

    @ApiModelProperty("标签名称")
    private String itemName;
}