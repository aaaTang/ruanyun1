package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class ShopParticularsParameterVO {

    private  String id ;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 个人简介
     */
    @Column(length = 500)
    private String individualResume;

    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String avatar;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 关注数量
     */
    private Integer followAttentionNum;

    /**
     * 评价数量
     */
    private Integer evaluateNum;


    /**
     * 是否关注 0 否 1 是
     */
    private Integer myFollowAttention;
}
