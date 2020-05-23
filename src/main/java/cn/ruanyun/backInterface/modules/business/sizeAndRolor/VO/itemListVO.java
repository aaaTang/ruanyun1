package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class itemListVO {

    //规格id
    private String keyId;

    //规格名称
    private String keyName;

    //规格属性id
    private String valId;

    //规格属性名称
    private String valName;

    //商品价格
    private BigDecimal goodPrice;

    //商品定金
    private BigDecimal goodDeposit;

    //商品尾款
    private BigDecimal goodDalancePayment;


}
