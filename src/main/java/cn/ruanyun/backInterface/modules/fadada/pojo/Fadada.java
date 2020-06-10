package cn.ruanyun.backInterface.modules.fadada.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 法大大
 * @author z
 */
@Data
@Entity
@Table(name = "t_fadada")
@TableName("t_fadada")
public class Fadada extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务分类")
    private String goodCategoryId;

    @ApiModelProperty("乙方合同标题")
    private String partTwoDocTitle;

    @ApiModelProperty("甲方合同标题")
    private String partOneDocTitle;

    @ApiModelProperty(value = "合同编号")
    private String contractId;

    @ApiModelProperty("合同公网下载地址")
    private String docUrl;

    @ApiModelProperty("是否归档")
    private BooleanTypeEnum contractFiling = BooleanTypeEnum.NO;

    @ApiModelProperty(value = "乙方签署人")
    private String partTwoExtSignUserId;
}