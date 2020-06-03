package cn.ruanyun.backInterface.modules.business.shopWorks.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家作品
 * @author z
 */
@Data
@Entity
@Table(name = "t_shop_works")
@TableName("t_shop_works")
public class ShopWorks extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 视频标题
     */
    private String title;


    /**
     * 视频
     */
    private String video;
}