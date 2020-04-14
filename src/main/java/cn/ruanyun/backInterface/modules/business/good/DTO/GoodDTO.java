package cn.ruanyun.backInterface.modules.business.good.DTO;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun-plus
 * @description: 商品筛选条件
 * @author: fei
 * @create: 2020-02-10 18:48
 **/
@Data
@Accessors(chain = true)
public class GoodDTO {

    /**
     * 销量升序 1，销量降序2
     * 价格升序3，价格价格4
     * 评论数升序5，评论数降序6
     */
    private Integer filterCondition;

    private GoodTypeEnum goodTypeEnum;

    /**
     * 商家id
     */
    private String storeId;

    /**
     * 价格最高
     */
    private BigDecimal priceHigh;


    /**
     * 价格最低
     */
    private BigDecimal priceLow = new BigDecimal(0);


    /**
     * 商品分类id
     */
    private String goodCategoryId;


    /**
     * 区域id
     */
    private String areaId;
}
