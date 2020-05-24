package cn.ruanyun.backInterface.modules.jpush.dto;

import cn.ruanyun.backInterface.common.enums.AudienceTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.PlatformTypeEnum;
import cn.ruanyun.backInterface.common.enums.PushTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-20 14:46
 **/
@Data
@Accessors(chain = true)
public class JpushDto {


    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 推送编号
     */
    private String jpushId;

    /**
     *推送类型
     */
    private PlatformTypeEnum platformType;

    /**
     * 推送目标类型
     */
    private AudienceTypeEnum audienceType;

    /**
     * 推送标签
     */
    private String tags;

    /**
     * 推送别名
     */
    private String alias;

    /**
     * 推送标题
     */
    private String title;

    /**
     * 推送内容
     */
    private String content;

    /**
     * 推送别名
     */
    private String secret;

    /**
     * 页号
     */
    private int pageNumber;

    /**
     * 页面大小
     */
    private int pageSize;

    /**
     * 审核状态
     */
    private CheckEnum checkEnum;

    /**
     * 审核原因
     */
    private String checkReason;

    /**
     * 推送类型
     */
    private PushTypeEnum pushType;

    /**
     * 推送值
     */
    private String pushValue;

}
