package cn.ruanyun.backInterface.modules.jpush.vo;

import cn.ruanyun.backInterface.common.enums.AudienceTypeEnum;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.PlatformTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-20 15:31
 **/

@Data
@Accessors(chain = true)
public class JpushVo {

    private String id;

    /**
     * 推送标题
     */
    private String title;

    /**
     * 推送内容
     */
    private String content;

    /**
     *推送类型
     */
    private PlatformTypeEnum platformType;

    /**
     * 推送目标类型
     */
    private AudienceTypeEnum audienceType;

    /**
     * 审核状态
     */
    private CheckEnum checkStatus = CheckEnum.PRE_CHECK;


    /**
     * 是否推送
     */
    private BooleanTypeEnum pushSuccess;


    /**
     * 审核原因
     */
    private String checkReason;

    /**
     * 审核时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}