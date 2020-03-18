package cn.ruanyun.backInterface.modules.base.vo;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BackUserVO {

    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 婚期
     */
    private String weddingDay;

    /**
     * 个人简介
     */
    private String individualResume;

    /**
     * 个人余额
     */
    private BigDecimal Balance;

    /**
     * 邀请码
     */
    private String invitationCode = CommonUtil.getRandomNum();

    /**
     * 用户头像
     */
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    /**
     * 用户类型 0普通用户 1管理员
     */
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    /**
     * 状态 默认0正常 -1拉黑
     */
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

}
