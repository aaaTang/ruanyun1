package cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 严选商家
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_best_shop")
@TableName("t_best_shop")
@Accessors(chain = true)
public class BestShop extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 申请成为商家ID
     */
    private String storeAuditId;

    /**
     * 是否严选，0否，1是
     */
    private Integer strict = 0;
}