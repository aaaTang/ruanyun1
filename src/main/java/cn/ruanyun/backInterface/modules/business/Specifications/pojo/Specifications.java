package cn.ruanyun.backInterface.modules.business.Specifications.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品规格
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_specifications")
@TableName("t_specifications")
public class Specifications extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 规格（比如颜色，名字多样性，不确定）
     */
    private String specifications;
    /**
     * 尺码
     */
    private String size;
    /**
     * 库存
     */
    private Integer number;


}