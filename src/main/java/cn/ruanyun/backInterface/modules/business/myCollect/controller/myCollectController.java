package cn.ruanyun.backInterface.modules.business.myCollect.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.myCollect.pojo.myCollect;
import cn.ruanyun.backInterface.modules.business.myCollect.service.ImyCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 我的收藏管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/myCollect")
@Transactional
public class myCollectController {

    @Autowired
    private ImyCollectService imyCollectService;


   /**
     * 更新或者插入数据
     * @param myCollect
     * @return
    */
    @PostMapping(value = "/insertOrderUpdatemyCollect")
    public Result<Object> insertOrderUpdatemyCollect(myCollect myCollect){

        try {

            imyCollectService.insertOrderUpdatemyCollect(myCollect);
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
    @PostMapping(value = "/removemyCollect")
    public Result<Object> removemyCollect(String ids){

        try {

            imyCollectService.removemyCollect(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
