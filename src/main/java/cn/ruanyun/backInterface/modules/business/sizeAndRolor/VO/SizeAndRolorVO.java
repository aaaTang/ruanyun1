package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class SizeAndRolorVO {

    private String id;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品颜色
     */
    private String color;
    /**
     * 商品图片
     */
    private String pic;

    /**
     * 是否为父级0否，1是
     */
    private Integer isParent;

    /**
     * 商品尺寸
     */
    private List<SizeVO> sizeList;

    /**
     * 商品数量
     */
    private AtomicReference<Integer> inventory;
}