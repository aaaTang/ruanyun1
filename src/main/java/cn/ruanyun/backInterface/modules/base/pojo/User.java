package cn.ruanyun.backInterface.modules.base.pojo;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AuthenticationTypeEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user")
@TableName("t_user")
@ApiModel(value = "用户")
@Accessors(chain = true)
public class User extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;



    /*--------------------个人基本信息--------------*/

    @ApiModelProperty("用户名")
    @Column(unique = true, nullable = false)
    private String username;

    @ApiModelProperty("用戶昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    @Column(length = 1000)
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮箱")
    private String email;


    /*--------------------消费者基本信息--------------*/

    @ApiModelProperty("婚期")
    private String weddingDay;

    @ApiModelProperty("个人简介")
    @Column(length = 500)
    private String individualResume;

    @ApiModelProperty("个人余额")
    private BigDecimal balance = new BigDecimal(0);

    @ApiModelProperty("邀请码")
    private String invitationCode = CommonUtil.getRandomNum();

    @ApiModelProperty("im的token")
    private String imToken;

    @ApiModelProperty("区域id")
    private String areaId;

    @ApiModelProperty("余额支付密码")
    private String payPassword;

    @ApiModelProperty("微信openId")
    private String openId;


    /*--------------------用户类型--------------*/

    @ApiModelProperty("用户类型 0普通用户 1管理员")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ApiModelProperty("状态 默认0正常 -1拉黑")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    /*--------------------商家信息--------------*/

    @ApiModelProperty(value = "轮播图")
    @Column(length = 1000)
    private String  pic ;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺营业时间")
    private String openTime;

    @ApiModelProperty("分类服务类型")
    private String classId;

    @ApiModelProperty("支付宝账号")
    private String alipayAccount;

    @ApiModelProperty("微信账号")
    private String wechatAccount;

    @ApiModelProperty("分数")
    private Integer score = 0;

    @ApiModelProperty(value = "到店礼")
    @Column(length = 1000)
    private String toStoreGift;

    @ApiModelProperty("开始时间,结束时间")
    private String businessHours = "暂未设置时间！";

    @ApiModelProperty(value = "订单礼")
    @Column(length = 1000)
    private String orderGift;

    /*--------------------商家标签--------------*/

    @ApiModelProperty("信任标识  0无  1有 ")
    private Integer trustIdentity = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty("连锁认证  0无  1有 ")
    private AuthenticationTypeEnum authenticationTypeEnum;
}
