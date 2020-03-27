package cn.ruanyun.backInterface.modules.business.size.entity;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
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
@Table(name = "t_size")
@TableName("t_size")
@ApiModel(value = "商品尺寸")
public class Size extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private String goodCategoryId;

    /**
     * 尺寸名称
     */
    private String name;


}