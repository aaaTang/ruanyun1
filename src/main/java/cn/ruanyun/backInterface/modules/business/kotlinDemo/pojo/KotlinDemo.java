package cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * kotlin栗子
 * @author z
 */
@Data
@Entity
@Table(name = "t_kotlin_demo")
@TableName("t_kotlin_demo")
public class KotlinDemo extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


}