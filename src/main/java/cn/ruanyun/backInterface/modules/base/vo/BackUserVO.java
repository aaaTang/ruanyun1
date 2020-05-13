package cn.ruanyun.backInterface.modules.base.vo;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class BackUserVO {

    private String id;

//    /**
//     * 用户名
//     */
//    private String username;
//
//    /**
//     * 用戶昵称
//     */
//    private String nickName;
//    /**
//     * 手机
//     */
//    private String mobile;
//
//    /**
//     * 性别
//     */
//    private String sex;
//
//    /**
//     * 地址
//     */
//    private String address;
//    /**
//     * 邮箱
//     */
//    private String email;
//
//    /**
//     * 婚期
//     */
//    private String weddingDay;
//
//    /**
//     * 个人简介
//     */
//    private String individualResume;
//
//    /**
//     * 个人余额
//     */
//    private BigDecimal Balance;
//
//    /**
//     * 邀请码
//     */
//    private String invitationCode = CommonUtil.getRandomNum();
//
//    /**
//     * 用户头像
//     */
//    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;
//
//    /**
//     * 用户类型 0普通用户 1管理员
//     */
//    private Integer type = CommonConstant.USER_TYPE_NORMAL;
//
//    /**
//     * 状态 默认0正常 -1拉黑
//     */
//    private Integer status = CommonConstant.USER_STATUS_NORMAL;
//
//    /**
//     * 轮播图
//     */
//    @Column(length = 1000)
//    private String  pic ;
//
//    /**
//     * 经度
//     */
//    private String longitude;
//
//    /**
//     * 纬度
//     */
//    private String latitude;
//
//    /**
//     * 店铺名称
//     */
//    private String shopName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 用户名
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 用戶昵称
     */
    private String nickName;

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
     * 邮箱
     */
    private String email;
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
     * 个人余额
     */
    private BigDecimal Balance = new BigDecimal(0);


    /**
     * 邀请码
     */
    private String invitationCode = CommonUtil.getRandomNum();

    /**
     * 用户头像
     */
    @Column(length = 1000)
//    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;
    private String avatar;

    /**
     * 用户类型 0普通用户 1管理员
     */
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    /**
     * 状态 默认0正常 -1拉黑
     */
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    /**
     * 轮播图
     */
    @Column(length = 1000)
    private String  pic ;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * im的token
     */
    private String imToken;

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 分类服务类型
     */
    private String classId;

    /**
     * 分类服务名称
     */
    private String className;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 微信账号
     */
    private String wechatAccount;

    /**
     *是否严选 0：不是 1：是
     */
    private Integer isBest;

    /**
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证反面
     */
    private String idCardBack;

    /**
     * 营业执照
     */
    private String businessCard;
}
