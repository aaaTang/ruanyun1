package cn.ruanyun.backInterface.modules.base.vo;

import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class BackUserInfo {

    private String id;
    /**
     * 用户名
     */
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
     * 用户头像
     */
    private String avatar;
    /**
     * 用户类型 0普通用户 1管理员
     */
    private String type;

    /**
     * 状态 默认0正常 -1拉黑
     */
    private Integer status;

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
     * 用户的角色信息
     */
    private List<Role> roles;

    /**
     * 用户的权限信息
     */
    private List<Permission> permissions;

    /**
     * 个人余额
     */
    private BigDecimal Balance;

    /**
     * 服务分类名称
     */
    private String serviceCategoryName;

}
