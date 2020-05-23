package cn.ruanyun.backInterface.modules.business.group.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 群组列表
 * @author z
 */
@Data
@Entity
@Table(name = "t_group")
@TableName("t_group")
public class Group extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    private String groupId;

    private String groupName;

    private String userId;

    private String storeId;

    private String storeServicerId;

    private String platformServicerId;
}