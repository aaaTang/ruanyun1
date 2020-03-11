package cn.ruanyun.backInterface.modules.base.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_message")
@TableName("t_message")
@ApiModel(value = "消息")
@Accessors(chain = true)
public class Message extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "消息类型")
    private String type;

    @ApiModelProperty(value = "新创建账号也推送")
    private Boolean createSend;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "发送范围")
    private Integer range;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "发送指定用户id")
    private String userIds;
}