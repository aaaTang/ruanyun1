package cn.ruanyun.backInterface.modules.business.myFavorite.entity;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_my_favorite")
@TableName("t_my_favorite")
@ApiModel(value = "我的收藏")
public class MyFavorite extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 类型
     */
    private GoodTypeEnum goodTypeEnum;

}