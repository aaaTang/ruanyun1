package cn.ruanyun.backInterface.modules.business.myFootprint.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户足迹
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_my_footprint")
@TableName("t_my_footprint")
public class MyFootprint extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 商家id
     */
    private String storeId;

    /**
     * 商品id
     */
    private String goodsId;
}