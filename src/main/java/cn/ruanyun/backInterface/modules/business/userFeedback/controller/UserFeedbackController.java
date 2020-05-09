package cn.ruanyun.backInterface.modules.business.userFeedback.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userFeedback.pojo.UserFeedback;
import cn.ruanyun.backInterface.modules.business.userFeedback.service.IUserFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
