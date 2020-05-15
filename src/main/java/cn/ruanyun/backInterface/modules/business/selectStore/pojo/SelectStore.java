package cn.ruanyun.backInterface.modules.business.selectStore.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品
 * @author fei
 */
@Data
@Entity
@Table(name = "t_select_store")
@TableName("t_select_store")
public class SelectStore extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 严选商家id
     */
    private String userId;

    /**
     * 是否严选，0否，1是
     */
    private Integer strict = 1;

}