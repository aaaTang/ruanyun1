package cn.ruanyun.backInterface.modules.business.comment.entity;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_comment")
@TableName("t_comment")
@ApiModel(value = "评论")
public class Comment extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    private String pid = CommonConstant.PARENT_ID;

    /**
     * 评价星级
     */
    private Integer startLevel;

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 订单id
     */
    private String orderId;

}