package cn.ruanyun.backInterface.modules.business.good.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppShopVO {

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺头像
     */
    private String shopPic;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 关注店铺人数
     */
    private Integer followAttentionNum;

    /**
     * 评论数量
     */
    private Integer commonNum;

}
