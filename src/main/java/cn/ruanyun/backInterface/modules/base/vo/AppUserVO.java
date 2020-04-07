package cn.ruanyun.backInterface.modules.base.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * 首页用户信息展示
 */
@Data
@Accessors(chain = true)
public class AppUserVO {


    private String id;


    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;


    /**
     * 角色名
     */
    private String roleName;


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
     * 我的收藏数量
     */
    private Long myCollectNum = 0L;


    /**
     * 我的足迹数量
     */
    private Long myFootprintNum = 0L;


    /**
     * 我的粉丝数量
     */
    private Long myFansNum = 0L;


    /**
     * 我的关注数量
     */
    private Long myAttentionNum = 0L;


    /**
     * 我的余额
     */
    private BigDecimal myBalance = new BigDecimal(0);


    /**
     * 我的额度
     */
    private BigDecimal myLimit = new BigDecimal(0);


}
