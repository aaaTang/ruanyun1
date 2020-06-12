package cn.ruanyun.backInterface.modules.business.selectStore.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-03-27 22:53
 **/

@Data
@Accessors(chain = true)
public class SelectStoreListVO {

    private String id;

    @ApiModelProperty("店铺名称")
    private String username;

    @ApiModelProperty("区域id")
    private String areaId;

    @ApiModelProperty("店铺图片")
    private String avatar;

    @ApiModelProperty(value = "商家类型 （1，酒店 2.主持人 3.默认）")
    private Integer storeType;

    @ApiModelProperty("最低价格")
    private BigDecimal lowPrice;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
