package cn.ruanyun.backInterface.modules.business.goodService.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品服务
 * @author z
 */
@Data
@Entity
@Table(name = "t_good_service")
@TableName("t_good_service")
public class GoodService extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品服务
     */
    private String serverName;
}