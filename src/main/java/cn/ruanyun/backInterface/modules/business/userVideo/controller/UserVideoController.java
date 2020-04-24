package cn.ruanyun.backInterface.modules.business.userVideo.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userVideo.DTO.UserVideoDTO;
import cn.ruanyun.backInterface.modules.business.userVideo.pojo.UserVideo;
import cn.ruanyun.backInterface.modules.business.userVideo.service.IUserVideoService;
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
 * 用户视频管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userVideo")
@Transactional
public class UserVideoController {

    @Autowired
    private IUserVideoService iUserVideoService;


   /**
     * 更新或者插入数据
     * @param userVideo
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserVideo")
    public Result<Object> insertOrderUpdateUserVideo(UserVideo userVideo){

        try {

            iUserVideoService.insertOrderUpdateUserVideo(userVideo);
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
    @PostMapping(value = "/removeUserVideo")
    public Result<Object> removeUserVideo(String ids){

        try {

            iUserVideoService.removeUserVideo(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 获取用户视频列表数据
     * @param pageVo
     * @return
     */
    @PostMapping(value = "/getUserVideoList")
    public Result<Object> getUserVideoList(PageVo pageVo, UserVideoDTO userVideoDTO){

        return Optional.ofNullable(iUserVideoService.getUserVideoList(userVideoDTO))
                .map(userVideo -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",userVideo.size());
                    result.put("data", PageUtil.listToPage(pageVo,userVideo));
                    return new ResultUtil<>().setData(result,"获取订单列表成功！");
        }).orElse(new ResultUtil<>().setErrorMsg("暂无数据"));
    }





}
