package cn.ruanyun.backInterface.modules.business.comment.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评论
 * @author wj
 */
@Data
@Entity
@Table(name = "t_comment")
@TableName("t_comment")
public class Comment extends RuanyunBaseEntity {
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
     * 商家id
     */
    private String userId;

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 1商品，2套餐
     */
    private GoodTypeEnum typeEnum;

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