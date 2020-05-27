package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class itemListVO {

    //规格属性
    List<itemVO> itemVO;
/*

    //商品价格
    private BigDecimal goodPrice;

    //商品定金
    private BigDecimal goodDeposit;

    //商品尾款
    private BigDecimal goodDalancePayment;
*/


}
