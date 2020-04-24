package cn.ruanyun.backInterface.modules.business.userDynamic.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户动态
 * @author z
 */
@Data
@Entity
@Table(name = "t_user_dynamic")
@TableName("t_user_dynamic")
public class UserDynamic extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 动态内容
     */
        private String title;
    /**
     * 动态图片
     */
    private String pic;

}