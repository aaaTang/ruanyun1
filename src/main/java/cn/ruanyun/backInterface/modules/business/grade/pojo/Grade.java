package cn.ruanyun.backInterface.modules.business.grade.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评分
 * @author wj
 */
@Data
@Entity
@Table(name = "t_grade")
@TableName("t_grade")
public class Grade extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private String userId;

    /**
     * 星星数
     */
    private Double startLevel;

}