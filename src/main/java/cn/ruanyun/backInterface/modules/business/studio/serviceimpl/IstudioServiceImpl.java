package cn.ruanyun.backInterface.modules.business.studio.serviceimpl;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.inviteMessage.pojo.InviteMessage;
import cn.ruanyun.backInterface.modules.business.inviteMessage.service.IInviteMessageService;
import cn.ruanyun.backInterface.modules.business.studio.dto.StudioDto;
import cn.ruanyun.backInterface.modules.business.studio.mapper.StudioMapper;
import cn.ruanyun.backInterface.modules.business.studio.pojo.Studio;
import cn.ruanyun.backInterface.modules.business.studio.service.IstudioService;
import cn.ruanyun.backInterface.modules.business.studio.vo.StudioListVo;
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

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 工作室接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IstudioServiceImpl extends ServiceImpl<StudioMapper, Studio> implements IstudioService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IInviteMessageService inviteMessageService;

    @Override
    public void insertOrderUpdateStudio(Studio studio) {

        if (ToolUtil.isEmpty(studio.getCreateBy())) {

            studio.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            studio.setUpdateBy(securityUtil.getCurrUser().getId());
        }

        if (this.save(studio)) {

            //1. 异步发送消息
            CompletableFuture.runAsync(() -> {

                InviteMessage inviteMessage = new InviteMessage();
                inviteMessage.setInitiatorUserId(studio.getCreateBy());
                inviteMessage.setStudioId(studio.getId());
                inviteMessage.setCreateBy(studio.getMemberId());
                inviteMessageService.save(inviteMessage);
            });

        }
    }

    @Override
    public void removeStudio(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public Result<Object> resolveInvite(Studio studio) {

        return Optional.ofNullable(this.getById(studio.getId())).map(studioUpdate -> {

            studioUpdate.setAgree(studio.getAgree());
            studioUpdate.setUpdateBy(securityUtil.getCurrUser().getId());

            this.updateById(studioUpdate);


            //删除邀请信息
            inviteMessageService.remove(Wrappers.<InviteMessage>lambdaQuery()
            .eq(InviteMessage::getStudioId, studio.getId()));

            return new ResultUtil<>().setSuccessMsg("处理邀请信息成功！");
        }).orElse(new ResultUtil<>().setErrorMsg(201, "当前邀请信息不存在！"));
    }

    @Override
    public Result<DataVo<StudioListVo>> getStudioList(PageVo pageVo, StudioDto studioDto) {

        Page<Studio> studioPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Studio>lambdaQuery()
        .eq(Studio::getAgree, BooleanTypeEnum.YES)
        .eq(Studio::getCreateBy, securityUtil.getCurrUser().getId())
        .orderByDesc(Studio::getCreateTime));


        if (ToolUtil.isEmpty(studioPage.getRecords())) {

            return new ResultUtil<DataVo<StudioListVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<StudioListVo> result = new DataVo<>();
        result.setDataResult(studioPage.getRecords().parallelStream().flatMap(studio -> {

            StudioListVo studioListVo = new StudioListVo();

            Optional.ofNullable(userService.getById(studio.getMemberId())).ifPresent(user ->
                    ToolUtil.copyProperties(user, studioListVo));

            ToolUtil.copyProperties(studio, studioListVo);

            return Stream.of(studioListVo);
        }).collect(Collectors.toList())).setTotalSize(studioPage.getTotal())
                .setCurrentPageNum(studioPage.getCurrent())
                .setTotalPage(studioPage.getPages());

        return new ResultUtil<DataVo<StudioListVo>>().setData(result, "获取团队信息成功！");
    }

}