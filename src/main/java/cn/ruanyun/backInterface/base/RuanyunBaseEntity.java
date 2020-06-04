package cn.ruanyun.backInterface.base;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fei
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public abstract class RuanyunBaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @TableId
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    @ApiModelProperty(value = "创建者", hidden = true)
    @CreatedBy
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = true)
    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(value = "删除标志 默认0", hidden = true)
    private Integer delFlag = CommonConstant.STATUS_NORMAL;

}
