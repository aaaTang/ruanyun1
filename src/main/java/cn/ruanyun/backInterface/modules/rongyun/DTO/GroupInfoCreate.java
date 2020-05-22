package cn.ruanyun.backInterface.modules.rongyun.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GroupInfoCreate {

    /**
     * 群组用户
     */
    private List<GroupUser> groupUsers;

    /**
     * 群组id
     */
    private String groupId;

    /**
     * 群组名称
     */
    private String groupName;

}
