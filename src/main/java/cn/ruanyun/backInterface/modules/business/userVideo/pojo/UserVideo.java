package cn.ruanyun.backInterface.modules.business.userVideo.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户视频
 * @author z
 */
@Data
@Entity
@Table(name = "t_user_video")
@TableName("t_user_video")
public class UserVideo extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 用户视频标题
     */
    private String title;


    /**
     * 用户视频
     */
    private String video;
}