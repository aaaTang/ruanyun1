package cn.ruanyun.backInterface.modules.business.followAttention.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodFollowAttentionVO {


    /**
     * 商家id
     */
    private String id;
    /**
     * 商家图片
     */
    private String avatar;
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 地址
     */
    private String address;

    /**
     * 评分
     */
    private BigDecimal gradedNum;

    /**
     *  评论数量
     */
    private String commentNum;
}
