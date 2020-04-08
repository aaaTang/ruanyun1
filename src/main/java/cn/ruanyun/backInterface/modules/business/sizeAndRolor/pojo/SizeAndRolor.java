package cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 规格和大小
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_size_and_rolor")
@TableName("t_size_and_rolor")
public class SizeAndRolor extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品尺寸
     */
    private String size;

    /**
     * 商品颜色
     */
    private String color;

    /**
     * 商品数量
     */
    private Integer inventory;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 父id
     */
    private String parentId;


    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;
}