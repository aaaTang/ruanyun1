package cn.ruanyun.backInterface.modules.business.studio.vo;

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
 * @create: 2020-05-28 20:08
 **/

@Data
@Accessors(chain = true)
public class StudioListVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("商家名称")
    private String nickName;

    @ApiModelProperty("商家头像")
    private String avatar;

    @ApiModelProperty(value = "商家手机号")
    private String mobile;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加入时间", hidden = true)
    private Date createTime;

}
