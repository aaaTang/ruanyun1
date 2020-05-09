package cn.ruanyun.backInterface.modules.business.aboutUs.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关于我们
 * @author z
 */
@Data
@Entity
@Table(name = "t_about_us")
@TableName("t_about_us")
public class AboutUs extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 图片
     */
    private String pic;
    /**
     * 名称
     */
    private String name;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 号码
     */
    private String mobile;
}