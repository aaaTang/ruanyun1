package cn.ruanyun.backInterface.modules.business.commodity.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品管理
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_commodity")
@TableName("t_commodity")
public class Commodity extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 商品名称
     */
    private String commodityName;

//    /**
//     * 商品满减
//     */
//    private String fullReduce;
    /**
     * 商品新价格
     */
    private String newprice;
    /**
     * 商品旧价格
     */
    private String oldPrice;

    /**
     * 商品轮播图
     */
    @Column(length = 1000)
    private String graphicDetails;

//    /**
//     * 商品规格
//     */
//    private String Specifications;

    /**
     * 商品详情
     */
    @Column(length = 1000)
    private String details;

    /**
     * 分类id
     */
    private String classificationId;
//    /**
//     * 销售量
//     */
//    private Integer salesVolume;

}