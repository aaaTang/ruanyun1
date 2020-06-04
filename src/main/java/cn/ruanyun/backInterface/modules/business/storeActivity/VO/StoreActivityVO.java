package cn.ruanyun.backInterface.modules.business.storeActivity.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class StoreActivityVO {


    private String id;
    /**
     * 活动名称
     */
    private String title;
    /**
     * 活动背景图
     */
    private String pic;
    /**
     * 内容
     */
    private String content;

    /**
     * 商家名称
     */
    private String shopName;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
