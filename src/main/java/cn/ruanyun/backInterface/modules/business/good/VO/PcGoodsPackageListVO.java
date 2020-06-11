package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO.GoodsIntroduceVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class PcGoodsPackageListVO {

    private  String id;
    private GoodTypeEnum typeEnum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
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

    /***
     * 分类名称
     */
    private String goodCategoryName;

    /**
     * 商品介绍
     */
    private List productsIntroduction;

    /**
     * 购买须知
     */
    private List purchaseNotes;
    /**
     * 商品是否删除
     */
    private Integer delFlag;
    /***
     * 商家名称
     */
    private String shopName;

    /**
     * 商家状态 默认0正常 -1拉黑
     */
    private Integer status;

    /**
     * 是否是推荐商品    0不是  1 是
     */
    private Integer recommend;
}
