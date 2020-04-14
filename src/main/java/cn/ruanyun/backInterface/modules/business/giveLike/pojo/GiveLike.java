package cn.ruanyun.backInterface.modules.business.giveLike.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户点赞
 * @author z
 */
@Data
@Entity
@Table(name = "t_give_like")
@TableName("t_give_like")
public class GiveLike extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 动态视频id
     */
    private String dynamicVideoId;

}