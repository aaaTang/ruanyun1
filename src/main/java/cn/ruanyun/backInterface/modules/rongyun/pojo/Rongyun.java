package cn.ruanyun.backInterface.modules.rongyun.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 融云
 * @author fei
 */
@Data
@Entity
@Table(name = "t_rongyun")
@TableName("t_rongyun")
public class Rongyun extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


}