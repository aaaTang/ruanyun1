package cn.ruanyun.backInterface.modules.business.Specifications.SpecificationsVO;

import lombok.Data;

@Data
public class SpecificationsListVO {

    private  String id;
    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 规格（比如颜色，名字多样性，不确定）
     */
    private String specifications;
    /**
     * 尺码
     */
    private String size;
    /**
     * 库存
     */
    private Integer number;
}
