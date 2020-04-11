package cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

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
     * 规格和规格属性id
     */
    private String attrSymbolPath;

    /**
     *  商品价格
     */
    private BigDecimal goodPrice = new BigDecimal(0);

    /**
     * 商品数量
     */
    private Integer inventory;

    /**
     *图片
     */
    private String pic;
}