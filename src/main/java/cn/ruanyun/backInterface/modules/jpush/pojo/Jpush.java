package cn.ruanyun.backInterface.modules.jpush.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AudienceTypeEnum;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.PlatformTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 虚拟号
 * @author z
 */
@Data
@Entity
@Table(name = "t_jpush")
@TableName("t_jpush")
@Accessors(chain = true)
public class Jpush extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 推送标题
     */
    private String title;

    /**
     * 推送内容
     */
    private String content;

    /**
     * 审核状态
     */
    private CheckEnum checkEnum = CheckEnum.PRE_CHECK;

    /**
     * 是否推送
     */
    private BooleanTypeEnum pushSuccess = BooleanTypeEnum.NO;

    /**
     * 审核原因
     */
    private String checkReason;

    /**
     *推送类型
     */
    private PlatformTypeEnum platformType;

    /**
     * 推送目标类型
     */
    private AudienceTypeEnum audienceType;

    /**
     * 审核时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
}