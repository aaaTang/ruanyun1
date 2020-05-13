package cn.ruanyun.backInterface.modules.business.good.VO;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ShopAndPackageVO {

    /**
     * 商家id
     */
    private String id;

    /**
     * 商家头像
     */
    private String avatar;

    /**
     * 商家昵称
     */
    private String nickName;
    /**
     * 地址
     */
    private String address;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 评分
     */
    private Double grade;

    /**
     * 商品套餐集合
     */
    private List<AppGoodInfoVO> appGoodInfo;
}
