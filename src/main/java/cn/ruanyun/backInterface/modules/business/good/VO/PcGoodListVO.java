package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class PcGoodListVO {


    private  String id;
    private GoodTypeEnum typeEnum;

    /**
     * 分类id
     */
    private String goodCategoryId;

    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;


    /**
     * 商品详情
     */
    private String goodDetails;


    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 商品是否删除
     */
    private Integer delFlag;
    /***
     * 分类名称
     */
    private String goodCategoryName;

    /***
     * 商家名称
     */
    private String shopName;

    /**
     * 商家状态 默认0正常 -1拉黑
     */
    private Integer status;
}
