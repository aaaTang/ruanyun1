package cn.ruanyun.backInterface.modules.business.shopWorks.serviceimpl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PcShopWorksListVO {

    private String id;
    /**
     * 视频标题
     */
    private String title;

    /**
     * 作品图片
     */
    private String pic;

    /**
     * 视频
     */
    private String video;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 是否删除
     */
    private Integer delFlag;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
