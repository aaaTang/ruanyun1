package cn.ruanyun.backInterface.modules.business.good.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品
 * @author fei
 */
@Data
@Entity
@Table(name = "t_good")
@TableName("t_good")
public class Good extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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


    /**
     * 颜色数据
     */
    private String colorIds;


    /**
     * 尺寸信息
     */
    private String sizeIds;


    /**
     * 商品库存
     */
    private Integer inventory;


    /**
     * 积分
     */
    private Integer integral;


}