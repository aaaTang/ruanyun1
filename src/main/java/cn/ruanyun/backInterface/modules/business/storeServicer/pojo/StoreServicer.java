package cn.ruanyun.backInterface.modules.business.storeServicer.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 店铺客服
 * @author z
 */
@Data
@Entity
@Table(name = "t_store_servicer")
@TableName("t_store_servicer")
public class StoreServicer extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private String storeId;

    /**
     * 商家客服id
     */
    private String servicerId;
}