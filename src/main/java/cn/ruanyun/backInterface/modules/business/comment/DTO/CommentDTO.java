package cn.ruanyun.backInterface.modules.business.comment.DTO;

import lombok.Data;

/**
 * @program: ruanyun-plus
 * @description: 商品筛选条件
 * @author: fei
 * @create: 2020-02-10 18:48
 **/
@Data
public class CommentDTO {

    /**
     * 订单id
     */
    private String orderId;


    /**
     * 商品评论内容 包含，商品id,内容，星星，照片
     */
    private String comments;

    /**
     * 商家id
     */
    private String userId;

    /**
     * 商家星数
     */
    private String startLevel;

}
