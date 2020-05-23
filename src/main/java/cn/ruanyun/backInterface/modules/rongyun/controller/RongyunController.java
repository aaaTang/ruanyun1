package cn.ruanyun.backInterface.modules.rongyun.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 融云管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/rongyun")
@Transactional
public class RongyunController {

    @Autowired
    private IRongyunService iRongyunService;


    /**
     * 获取用户token
     * @param userId
     * @param name
     * @param portraitUri
     * @return
     * @throws RuanyunException
     */
    @PostMapping("/getToken")
    public Result<Object> getToken(String userId, String name, String portraitUri) throws RuanyunException {

        return new ResultUtil<>().setData(iRongyunService.getToken(userId, name, portraitUri),"获取token成功！");
    }


    /**
     * 判断用户在线状态
     * @param userId
     * @return
     * @throws RuanyunException
     */
    @PostMapping("/checkOnlineResult")
    public Result<Object> checkOnlineResult(String userId) throws RuanyunException {

        return new ResultUtil<>().setData(iRongyunService.checkOnlineResult(userId),"获取用户在线状态成功！");
    }

    /**
     * 创建群聊
     * @param userId 用户id
     * @param merchantId 商家id
     * @return Object
     */
    @PostMapping("/group/create")
    public Result<Object> createGroup (String userId, String merchantId){
        System.out.println("用户id" + userId + "商家id" + merchantId);
        return new ResultUtil<>().setData(iRongyunService.createGroup(userId, merchantId), "创建群组成功！");
    }

    /**
     * 更新群组信息
     * @param groupId
     * @param groupName
     * @return
     */
    @PostMapping("/group/update")
    public Result<Object> updateGroup (String groupId, String groupName){
        return new ResultUtil<>().setData(iRongyunService.updateGroup(groupId, groupName), "更新群组信息成功！");
    }

    /**
     * 加入群组
     * @param groupId
     * @param groupName
     * @param member
     * @return
     */
    @PostMapping("/group/join")
    public Result<Object> joinGroup (String groupId, String groupName, String[] member){
        return new ResultUtil<>().setData(iRongyunService.joinGroup(groupId, groupName, member), "加入群组成功！");
    }

    /**
     * 退出群组
     * @param groupId
     * @param member
     * @return
     */
    @PostMapping("/group/quit")
    public Result<Object> quitGroup (String groupId, String[] member){
        return new ResultUtil<>().setData(iRongyunService.quitGroup(groupId, member), "退出群组成功！");
    }

    /**
     * 解散群聊
     * @param groupId
     * @param userId
     * @return
     */
    @PostMapping("/group/dismiss")
    public Result<Object> dismissGroup (String groupId, String userId){
        return new ResultUtil<>().setData(iRongyunService.dismissGroup(groupId, userId), "更新群组信息成功！");
    }

    /**
     * 添加禁言群成员（在 App 中如果不想让某一用户在群中发言时，可将此用户在群组中禁言，被禁言用户可以接收查看群组中用户聊天信息，但不能发送消息。）
     * @param  userId  用户 Id。（必传）
     * @param  groupId  群组 Id。（必传）
     * @param  minute  禁言时长，以分钟为单位，最大值为43200分钟。（必传）
     */
    @PostMapping("/group/addGagUser")
    public Result<Object> addGagUser(String userId, String groupId, String minute){
        return new ResultUtil<>().setData(iRongyunService.addGagUser(userId, groupId, minute), "添加禁言群成员成功！");
    }

    /**
     * 查询被禁言群成员
     * @param  groupId  群组Id。（必传）
     **/
    @PostMapping("/group/lisGagUser")
    public Result<Object> lisGagUser(String groupId){
        return new ResultUtil<>().setData(iRongyunService.lisGagUser(groupId), "查询被禁言群成员成功！");
    }

    /**
     * 移除禁言群成员
     * @param  userIds  用户Id。支持同时移除多个群成员（必传）
     * @param  groupId  群组Id。（必传）
     **/
    @PostMapping("/group/rollBackGagUser")
    public Result<Object> rollBackGagUser(String[] userIds, String groupId){
        return new ResultUtil<>().setData(iRongyunService.rollBackGagUser(userIds, groupId), "移除禁言群成员成功！");
    }

    @PostMapping("/group/getUserByGroupId")
    public Result<Object> getUserByGroupId(String groupId){
        return new ResultUtil<>().setData(iRongyunService.getUserByGroupId(groupId), "获取群成员成功！");
    }
}
