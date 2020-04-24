package cn.ruanyun.backInterface.modules.business.userAnswer.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userAnswer.DTO.UserAnswerDTO;
import cn.ruanyun.backInterface.modules.business.userAnswer.pojo.UserAnswer;
import cn.ruanyun.backInterface.modules.business.userAnswer.service.IUserAnswerService;
import cn.ruanyun.backInterface.modules.business.userDynamic.DTO.UserDynamicDTO;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 用户问答管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userAnswer")
@Transactional
public class UserAnswerController {

    @Autowired
    private IUserAnswerService iUserAnswerService;


   /**
     * 更新或者插入数据
     * @param userAnswer
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserAnswer")
    public Result<Object> insertOrderUpdateUserAnswer(UserAnswer userAnswer){

        try {

            iUserAnswerService.insertOrderUpdateUserAnswer(userAnswer);
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
    @PostMapping(value = "/removeUserAnswer")
    public Result<Object> removeUserAnswer(String ids){

        try {

            iUserAnswerService.removeUserAnswer(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * App查询用户问答列表
     * @return
     */
    @PostMapping(value = "/getUserAnswer")
    public Result<Object> getUserAnswer(PageVo pageVo, UserAnswerDTO userAnswerDTO){

        return Optional.ofNullable(iUserAnswerService.getUserAnswer(userAnswerDTO))
                .map(userAnswer -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",userAnswer.size());
                    result.put("data", PageUtil.listToPage(pageVo,userAnswer));
                    return new ResultUtil<>().setData(result,"App查询用户问答列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg("暂无数据"));
    }

}
