package cn.ruanyun.backInterface.modules.base.pojo;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_user")
@TableName("t_user")
@ApiModel(value = "用户")
@Accessors(chain = true)
public class User extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 密码
     */
    private String password;

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
    @Column(length = 500)
    private String individualResume;

    /**
     * 密码强度
     */
    @Column(length = 2)
    private String passStrength;

    /**
     * 邀请码
     */
    private String invitationCode = CommonUtil.getRandomNum();

    /**
     * 用户头像
     */
    @Column(length = 1000)
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
