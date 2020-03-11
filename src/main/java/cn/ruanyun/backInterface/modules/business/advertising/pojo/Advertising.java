package cn.ruanyun.backInterface.modules.business.advertising.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 广告管理
 * @author fei
 */
@Data
@Entity
@Table(name = "t_advertising")
@TableName("t_advertising")
public class Advertising extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


}