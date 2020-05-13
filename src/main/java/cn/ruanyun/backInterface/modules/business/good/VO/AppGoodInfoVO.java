package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-03-27 23:22
 **/
@Data
@Accessors(chain = true)
public class AppGoodInfoVO {


    private String id;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;

    /**
     * 商品类型
     */
    private String typeEnum;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;
}
