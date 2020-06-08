package cn.ruanyun.backInterface.modules.business.good.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import dm.jdbc.stat.support.json.JSONArray;
import lombok.Data;

import javax.persistence.Column;
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
    @Column(length = 1000)
    private String goodPics;

    /**
     * 商品视频
     */
    @Column(length = 1000)
    private String goodVideo;

    /**
     * 商品视频展示图
     */
    @Column(length = 1000)
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

    /**
     * 积分
     */
    private Integer integral;

}