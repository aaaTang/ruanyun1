package cn.ruanyun.backInterface.modules.business.comment.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评价
 * @author fei
 */
@Data
@Entity
@Table(name = "t_common")
@TableName("t_common")
public class Common extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上级id
     */
    private String pid = CommonConstant.PARENT_ID;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 评价星级
     */
    private String startLevel;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片
     */
    private String pics;


}