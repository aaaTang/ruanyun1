package cn.ruanyun.backInterface.modules.business.giveLike.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.giveLike.pojo.GiveLike;
import cn.ruanyun.backInterface.modules.business.giveLike.service.IGiveLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 用户点赞管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/giveLike")
@Transactional
public class GiveLikeController {

    @Autowired
    private IGiveLikeService iGiveLikeService;


   /**
     * 更新或者插入数据
     * @param giveLike
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGiveLike")
    public Result<Object> insertOrderUpdateGiveLike(GiveLike giveLike){

        try {

            iGiveLikeService.insertOrderUpdateGiveLike(giveLike);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param dynamicVideoId
     * @return
    */
    @PostMapping(value = "/removeGiveLike")
    public Result<Object> removeGiveLike(String dynamicVideoId){

        try {

            iGiveLikeService.removeGiveLike(dynamicVideoId);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
