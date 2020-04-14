package cn.ruanyun.backInterface.modules.business.dynamicVideo.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DynamicTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 动态视频
 * @author z
 */
@Data
@Entity
@Table(name = "t_dynamic_video")
@TableName("t_dynamic_video")
public class DynamicVideo extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;
    /**
     * 标签
     */
    private String label;
    /**
     * 视频，图片，文字
     */
    private String video;
    /**
     * 类型
     */
    private DynamicTypeEnum dynamicTypeEnum;


}