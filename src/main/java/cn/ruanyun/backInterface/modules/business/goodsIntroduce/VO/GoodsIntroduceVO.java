package cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO;

import lombok.Data;

@Data
public class GoodsIntroduceVO {


    private  String  id;
    /**
     * 商品套餐id
     */
    private  String  goodsPackageId;
    /**
     * 标题名称
     */
    private  String  title;

    /**
     * 1list  2 string  3 html
     */
    private  Integer Type;
    /**
     * 内容
     */
    private  String  content;
}
