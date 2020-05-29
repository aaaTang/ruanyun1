package cn.ruanyun.backInterface.modules.business.inviteMessage.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.inviteMessage.pojo.InviteMessage;
import cn.ruanyun.backInterface.modules.business.inviteMessage.service.IInviteMessageService;
import cn.ruanyun.backInterface.modules.business.inviteMessage.vo.InviteMessageListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fei
 * 邀请信息管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/inviteMessage")
@Transactional
@Api(tags = "邀请信息管理接口")
public class InviteMessageController {

    @Autowired
    private IInviteMessageService iInviteMessageService;


    @PostMapping(value = "/removeInviteMessage")
    @ApiOperation("移除邀请信息")
    public Result<Object> removeInviteMessage(String ids){

        try {

            iInviteMessageService.removeInviteMessage(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping(value = "/getInviteMessageList")
    @ApiOperation("获取我的邀请信息")
    public Result<DataVo<InviteMessageListVo>> getInviteMessageList(PageVo pageVo) {

        return iInviteMessageService.getInviteMessageList(pageVo);
    }

}
