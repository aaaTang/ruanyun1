package cn.ruanyun.backInterface.modules.rongyun.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrliangzhiming@foxmail.com at 2020/05/25 09:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserVO {
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
    private String nickName;
}
