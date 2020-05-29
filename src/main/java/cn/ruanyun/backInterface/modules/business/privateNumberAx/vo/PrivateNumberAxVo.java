package cn.ruanyun.backInterface.modules.business.privateNumberAx.vo;

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
 * @create: 2020-05-19 19:13
 **/
@Data
@Accessors(chain = true)
public class PrivateNumberAxVo {


    private String id;

    /**
     * 客户绑定名称
     */
    private String callerName;

    /**
     * 客户绑定手机号
     */
    private String callerPhone;

    /**
     * 商家绑定名称
     */
    private String calleeName;

    /**
     * 商家绑定手机号
     */
    private String calleePhone;

    /**
     * 虚拟号段
     */
    private String privateNumber;

    /**
     * 绑定ID，唯一标识一组绑定关系。成功响应时必定返回。请记录该ID用于后续接口调用。
     */
    private String subscriptionId;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
