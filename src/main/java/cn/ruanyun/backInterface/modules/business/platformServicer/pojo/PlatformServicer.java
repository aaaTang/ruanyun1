package cn.ruanyun.backInterface.modules.business.platformServicer.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 平台客服
 * @author z
 */
@Data
@Entity
@Table(name = "t_platform_servicer")
@TableName("t_platform_servicer")
public class PlatformServicer extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 平台客服id
     */
    private String servicerId;

}