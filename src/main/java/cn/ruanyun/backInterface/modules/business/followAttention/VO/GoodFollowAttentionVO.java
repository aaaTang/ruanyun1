package cn.ruanyun.backInterface.modules.business.followAttention.VO;

import lombok.Data;

@Data
public class GoodFollowAttentionVO {

    private  String  id;

    /**
     * 商家id
     */
    private String userId;
    /**
     * 商家图片
     */
    private  String  pic;
    /**
     * 商家名称
     */
    private  String  username;
    /**
     * 商家地址
     */
    private  String  address;
}
