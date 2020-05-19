package cn.ruanyun.backInterface.modules.business.good.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-03-27 22:39
 **/
@Data
@Accessors(chain = true)
public class AppGoodListVO {


    private String id;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPic;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 用户名
     */
    private String username;
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户头像
     */
    private String avatar;


    /**
     * 评分
     */
    private Double grade;


    /**
     * 评论数
     */
    private Integer commentNum;


    /**
     * 销量
     */
    private Integer SaleVolume = 0;


}
