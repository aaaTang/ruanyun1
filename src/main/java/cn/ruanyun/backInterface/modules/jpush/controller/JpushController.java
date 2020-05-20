package cn.ruanyun.backInterface.modules.jpush.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.AudienceTypeEnum;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.jpush.dto.JpushDto;
import cn.ruanyun.backInterface.modules.jpush.pojo.Jpush;
import cn.ruanyun.backInterface.modules.jpush.service.IJpushService;
import cn.ruanyun.backInterface.modules.jpush.vo.JpushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 虚拟号管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/jpush")
@Transactional
public class JpushController {

    @Autowired
    private IJpushService iJpushService;


    /**
     * 插入或者更新极光推送
     * @param jpush jpush
     * @return Object
     */
    @PostMapping("/insertOrUpdateJpush")
    public Result<Object> insertOrUpdateJpush(Jpush jpush) {

        try {
            iJpushService.insertOrUpdateJpush(jpush);
            return new ResultUtil<>().setSuccessMsg("添加推送成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }
    }

    /**
     * 移除推送
     * @param ids ids
     * @return Object
     */
    @PostMapping("/removeJpush")
    public Result<Object> removeJpush(String ids) {

        try {
            iJpushService.removeJpush(ids);
            return new ResultUtil<>().setSuccessMsg("移除推送成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }
    }

    /**
     * 获取推送列表
     * @param jpushDto jpushDto
     * @return Object
     */
    @PostMapping("/getJpushList")
    public Result<Object> getJpushList(JpushDto jpushDto) {

        DataVo<JpushVo> result = iJpushService.getJpushList(jpushDto);

        return Optional.ofNullable(result.getDataResult()).map(jpushVos ->
                new ResultUtil<>().setData(result, "获取推送文章数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 审核推送
     * @param jpushDto jpushDto
     * @return
     */
    @PostMapping("/checkJpush")
    public Result<Object> checkJpush(JpushDto jpushDto) {

        try {
            iJpushService.checkJpush(jpushDto);
            return new ResultUtil<>().setSuccessMsg("审核推送成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }
    }

    /**
     * 推送公告给客户
     * @param jpushDto 推送参数
     * @return object
     */
    @PostMapping("/pushArticleToUser")
    public Result<Object> pushArticleToUser(JpushDto jpushDto) {

       if (ToolUtil.isEmpty(jpushDto.getAudienceType())) {

           return Optional.ofNullable(iJpushService.getById(jpushDto.getId()))
                   .map(jpush -> {

                     ToolUtil.copyProperties(jpush, jpushDto);
                     return iJpushService.pushArticleToUser(jpushDto);
                   }).orElse(new ResultUtil<>().setErrorMsg(201, "当前推送不存在！"));
       }else {

          return iJpushService.pushArticleToUser(jpushDto);
       }

    }
}
