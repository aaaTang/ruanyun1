package cn.ruanyun.backInterface.modules.fadada.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 法大大
 * @author z
 */
@Data
@Entity
@Table(name = "t_fadada")
@TableName("t_fadada")
public class fadada extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


}