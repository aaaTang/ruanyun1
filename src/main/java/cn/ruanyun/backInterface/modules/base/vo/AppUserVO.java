package cn.ruanyun.backInterface.modules.base.vo;


import lombok.Data;
import lombok.experimental.Accessors;

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
     * 我的收藏数量
     */
    private Long myCollectNum;


    /**
     * 我的足迹数量
     */
    private Long myFootprintNum;


    /**
     * 我的粉丝数量
     */
    private Long myFansNum;


    /**
     * 我的关注数量
     */
    private Long myAttentionNum;


    /**
     * 我的余额
     */
    private BigDecimal myBalance;


    /**
     * 我的额度
     */
    private BigDecimal myLimit;


}
