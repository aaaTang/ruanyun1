package cn.ruanyun.backInterface.modules.fadada.vo;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-06-10 17:12
 **/
@Data
@Accessors(chain = true)
public class ElectronicContractVo {

    private String id;

    @ApiModelProperty("服务分类")
    private String goodCategoryName;

    @ApiModelProperty("合同标题")
    private String docTitle;

    @ApiModelProperty(value = "合同编号")
    private String contractId;

    @ApiModelProperty("合同公网下载地址")
    private String docUrl;

    @ApiModelProperty("是否归档")
    private BooleanTypeEnum contractFiling = BooleanTypeEnum.NO;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
