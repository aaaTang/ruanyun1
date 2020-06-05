package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;

import lombok.Data;

@Data
public class itemVO {


    //商品规格的id
    private String id;

    //规格id
    private String keyId;

    //规格名称
    private String keyName;

    //规格属性id
    private String valId;

    //规格属性名称
    private String valName;

}
