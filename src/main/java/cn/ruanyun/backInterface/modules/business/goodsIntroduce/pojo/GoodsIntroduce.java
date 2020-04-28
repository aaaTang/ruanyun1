package cn.ruanyun.backInterface.modules.business.goodsIntroduce.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品介绍
 * @author z
 */
@Data
@Entity
@Table(name = "t_goods_introduce")
@TableName("t_goods_introduce")
public class GoodsIntroduce extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 1商品线详情   2 购买须知
     */
    private  Integer introduceAndDuy;

    /**
     * 内容
     */
    private  String  content;




}