package cn.ruanyun.backInterface.modules.rongyun.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUser {
    /**
     * id
     */
    private String id;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 身份
     */
    private String status;

}
