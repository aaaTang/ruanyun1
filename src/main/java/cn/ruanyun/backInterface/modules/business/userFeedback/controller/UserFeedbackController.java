package cn.ruanyun.backInterface.modules.business.userFeedback.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userFeedback.pojo.UserFeedback;
import cn.ruanyun.backInterface.modules.business.userFeedback.service.IUserFeedbackService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 用户意见反馈管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userFeedback")
@Transactional
public class UserFeedbackController {

    @Autowired
    private IUserFeedbackService iUserFeedbackService;


   /**
     * 更新或者插入数据
     * @param userFeedback
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserFeedback")
    public Result<Object> insertOrderUpdateUserFeedback(UserFeedback userFeedback){

        try {

            iUserFeedbackService.insertOrderUpdateUserFeedback(userFeedback);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeUserFeedback")
    public Result<Object> removeUserFeedback(String ids){

        try {

            iUserFeedbackService.removeUserFeedback(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * 后端查询意见反馈列表
     * @return
     */
    @PostMapping(value = "/getFeedbackList")
    public Result<Object> getFeedbackList(PageVo pageVo, String id){

        return Optional.ofNullable(iUserFeedbackService.getFeedbackList(id))
                .map(feedbackList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",feedbackList.size());
                    result.put("data", PageUtil.listToPage(pageVo,feedbackList));
                    return new ResultUtil<>().setData(result, "查询意见反馈列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }



}
