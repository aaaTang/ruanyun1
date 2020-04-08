package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.modules.business.color.VO.ColorInfoVO;
import cn.ruanyun.backInterface.modules.business.size.VO.SizeInfoVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
    private String goodPic;


//    /**
//     * 尺寸
//     */
//    private List<SizeInfoVO> sizes;
//
//
//    /**
//     * 颜色
//     */
//    private List<ColorInfoVO> colors;
//
//
//    /**
//     * 商品库存
//     */
//    private Integer inventory;

}
