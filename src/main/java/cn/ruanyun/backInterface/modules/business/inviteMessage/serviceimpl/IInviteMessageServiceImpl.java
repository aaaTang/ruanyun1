package cn.ruanyun.backInterface.modules.business.inviteMessage.serviceimpl;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.inviteMessage.pojo.InviteMessage;
import cn.ruanyun.backInterface.modules.business.inviteMessage.vo.InviteMessageListVo;
import cn.ruanyun.backInterface.modules.business.inviteMessage.mapper.InviteMessageMapper;
import cn.ruanyun.backInterface.modules.business.inviteMessage.service.IInviteMessageService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 邀请信息接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IInviteMessageServiceImpl extends ServiceImpl<InviteMessageMapper, InviteMessage> implements IInviteMessageService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;


    @Override
    public void removeInviteMessage(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public Result<DataVo<InviteMessageListVo>> getInviteMessageList(PageVo pageVo) {

        Page<InviteMessage> inviteMessagePage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<InviteMessage>lambdaQuery()
                .eq(InviteMessage::getCreateBy, securityUtil.getCurrUser().getId())
                .orderByDesc(InviteMessage::getCreateTime));


        if (ToolUtil.isEmpty(inviteMessagePage.getRecords())) {

            return new ResultUtil<DataVo<InviteMessageListVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<InviteMessageListVo> result = new DataVo<>();
        result.setDataResult(inviteMessagePage.getRecords().parallelStream().flatMap(inviteMessage -> {

            InviteMessageListVo inviteMessageListVo = new InviteMessageListVo();

            Optional.ofNullable(userService.getById(inviteMessage.getInitiatorUserId())).ifPresent(user ->
                    ToolUtil.copyProperties(user, inviteMessageListVo));

            ToolUtil.copyProperties(inviteMessage, inviteMessageListVo);

            return Stream.of(inviteMessageListVo);
        }).collect(Collectors.toList())).setTotalSize(inviteMessagePage.getTotal())
                .setCurrentPageNum(inviteMessagePage.getCurrent())
                .setTotalPage(inviteMessagePage.getPages());

        return new ResultUtil<DataVo<InviteMessageListVo>>().setData(result, "获取邀请信息信息成功！");

    }

}