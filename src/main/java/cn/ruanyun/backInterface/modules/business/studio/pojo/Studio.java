package cn.ruanyun.backInterface.modules.business.studio.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工作室
 * @author fei
 */
@Data
@Entity
@Table(name = "t_studio")
@TableName("t_studio")
public class Studio extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成员id")
    private String memberId;

    @ApiModelProperty(value = "是否同意")
    private BooleanTypeEnum agree = BooleanTypeEnum.NO;
}