package cn.ruanyun.backInterface.modules.business.staffManagement.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 员工管理
 * @author z
 */
@Data
@Entity
@Table(name = "t_staff_management")
@TableName("t_staff_management")
@Accessors(chain = true)
public class StaffManagement extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 员工id
     */
    private String staffId;

}