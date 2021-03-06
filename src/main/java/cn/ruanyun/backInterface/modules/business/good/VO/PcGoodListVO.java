package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PcGoodListVO {


    private  String id;
    private GoodTypeEnum typeEnum;

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
     * 商品视频
     */
    private String goodVideo;

    /**
     * 商品视频展示图
     */
    private String goodVideoPic;
    /**
     * 商品详情
     */
    private String goodDetails;


    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 商品是否删除
     */
    private Integer delFlag;
    /***
     * 分类名称
     */
    private String goodCategoryName;

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

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;
}
