package cn.ruanyun.backInterface.modules.business.inviteMessage.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.inviteMessage.vo.InviteMessageListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.inviteMessage.pojo.InviteMessage;

/**
 * 邀请信息接口
 * @author fei
 */
public interface IInviteMessageService extends IService<InviteMessage> {


    /**
     * 移除inviteMessage
     * @param ids ids
     */
    void removeInviteMessage(String ids);


    /**
     * 获取邀请你列表
     * @param pageVo 分页
     * @return InviteMessageListVo
     */
    Result<DataVo<InviteMessageListVo>> getInviteMessageList(PageVo pageVo);

}