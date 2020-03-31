package cn.ruanyun.backInterface.modules.business.myCollect.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 我的收藏
 * @author fei
 */
@Data
@Entity
@Table(name = "t_my_collect")
@TableName("t_my_collect")
public class myCollect extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 商品id
     */
    private String goodId;

}