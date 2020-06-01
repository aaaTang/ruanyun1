package cn.ruanyun.backInterface.modules.merchant.shopServiceTag.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家优质服务标签
 * @author z
 */
@Data
@Entity
@Table(name = "t_shop_service_tag")
@TableName("t_shop_service_tag")
public class shopServiceTag extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 标签id
     */


    /**
     * 审核状态
     */
}