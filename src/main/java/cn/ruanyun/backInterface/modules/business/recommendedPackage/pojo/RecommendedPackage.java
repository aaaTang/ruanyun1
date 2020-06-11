package cn.ruanyun.backInterface.modules.business.recommendedPackage.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 推荐商品和套餐
 * @author z
 */
@Data
@Entity
@Table(name = "t_recommended_package")
@TableName("t_recommended_package")
public class RecommendedPackage extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodId;

}