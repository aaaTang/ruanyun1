package cn.ruanyun.backInterface.modules.rongyun.service;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupInfoCreate;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import com.baomidou.mybatisplus.extension.service.IService;
import io.rong.models.CodeSuccessResult;

import java.util.List;
import java.util.Map;

/**
 * 融云接口
 * @author fei
 */
public interface IRongyunService extends IService<Rongyun> {


    /**
     * 注册IM用户
     * @param id
     * @param name
     * @param portrait
     * @throws RuanyunException
     */
    void addUser(String id, String name, String portrait) throws RuanyunException;

    /**
     * 修改IM用户信息
     * @param id
     * @param name
     * @param portrait
     * @return
     * @throws RuanyunException
     */
    boolean updateUser(String id,String name,String portrait)throws RuanyunException;



    /**
     * 获取融云token
     * @param userId
     * @param name
     * @param portraitUri
     * @return
     * @throws RuanyunException
     */
    String getToken(String userId, String name, String portraitUri) throws RuanyunException;


    /**
     * 检查用户是否在线
     * @param userId
     * @return
     */
    String checkOnlineResult(String userId) throws RuanyunException;


    /**
     * 创建群组
     * @param userId 用户id
     * @param merchantId 商家id
     * @return GroupInfoCreate
     */
    GroupInfoCreate createGroup(String userId, String merchantId);

    /**
     * 加入群组
     * @param groupId 群组id
     * @param groupName 群组名称
     * @param member 要加入的成员ids
     * @return
     */
    Object joinGroup(String groupId, String groupName, String[] member);

    /**
     * 更新群组信息
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    Object updateGroup(String groupId, String groupName);

    /**
     * 退出群组
     * @param groupId 群组id
     * @param member 群组成员
     * @return
     */
    Object quitGroup(String groupId, String[] member);

    /**
     * 解散群组
     * @param groupId 群组id
     * @param userId 操作者id
     * @return
     */
    Object dismissGroup(String groupId, String userId);



    /**
     * 添加禁言群成员（在 App 中如果不想让某一用户在群中发言时，可将此用户在群组中禁言，被禁言用户可以接收查看群组中用户聊天信息，但不能发送消息。）
     * @param  userId  用户 Id。（必传）
     * @param  groupId  群组 Id。（必传）
     * @param  minute  禁言时长，以分钟为单位，最大值为43200分钟。（必传）
     * @return  CodeSuccessResult
     */
    Object addGagUser(String userId, String groupId, String minute);

    /**
     * 查询被禁言群成员
     * @param  groupId  群组Id。（必传）
     * @return ListGagGroupUserResult
     **/
    Object lisGagUser(String groupId);

    /**
     * 移除禁言群成员方法
     *
     * @param  userId  用户Id。支持同时移除多个群成员（必传）
     * @param  groupId  群组Id。（必传）
     *
     * @return  CodeSuccessResult
     **/
    Object rollBackGagUser(String[] userId, String groupId);

    /**
     * 获取群聊用户信息
     * @param groupId
     * @return
     */
    Map<String, Object> getUserByGroupId(String groupId);


    /**
     * 获取用户群组列表
     * @param userId
     * @return
     */
    Object getGroupListByUserId(String userId);
}

