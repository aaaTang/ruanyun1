package cn.ruanyun.backInterface.modules.business.goodsPackage.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品套餐
 * @author fei
 */
@Data
@Entity
@Table(name = "t_goods_package")
@TableName("t_goods_package")
public class GoodsPackage extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 套餐图片
     */
    private String pics;

    /**
     * 新价格
     */
    private String newPrice;

    /**
     * 旧价格
     */
    private String oldPrice;


    /**
     * 商品介绍
     */
    private String productsIntroduction;


    /**
     * 商品亮点
     */
    private String productLightspot;


    /**
     * 拍摄特色
     */
    private String shootCharacteristics;


    /**
     * 图文详情
     */
    @Column(length = 1000)
    private String graphicDetails;


    /**
     * 购买须知
     */
    private String purchaseNotes;


    /**
     * 温馨提示
     */
    private String warmPrompt;

    /**
     * 分类ID
     */
    private String classId;

    /**
     * 地区ID
     */
    private String areaId;


}