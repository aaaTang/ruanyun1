package cn.ruanyun.backInterface.modules.business.dynamicVideo.controller;

import cn.ruanyun.backInterface.common.enums.DynamicTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.pojo.DynamicVideo;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.service.IDynamicVideoService;
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
 * 动态视频管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/dynamicVideo")
@Transactional
public class DynamicVideoController {

    @Autowired
    private IDynamicVideoService iDynamicVideoService;


   /**
     * 更新或者插入数据
     * @param dynamicVideo
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateDynamicVideo")
    public Result<Object> insertOrderUpdateDynamicVideo(DynamicVideo dynamicVideo){

        try {

            iDynamicVideoService.insertOrderUpdateDynamicVideo(dynamicVideo);
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
    @PostMapping(value = "/removeDynamicVideo")
    public Result<Object> removeDynamicVideo(String ids){

        try {

            iDynamicVideoService.removeDynamicVideo(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 获取动态和视频列表数据
     * @param pageVo
     * @param userId  用户id
     * @param label   标签名称
     * @return
     */
    @PostMapping("/getDynamicVideo")
    public Result<Object> getDynamicVideo(PageVo pageVo, String userId, String label,DynamicTypeEnum dynamicType) {

        return Optional.ofNullable(iDynamicVideoService.getDynamicVideo(userId,label,dynamicType))
                .map(dynamicVideoList -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",dynamicVideoList.size());
                    result.put("data", PageUtil.listToPage(pageVo,dynamicVideoList));

                    return new ResultUtil<>().setData(result,"获取视频列表数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取问答数据列表
     * @param pageVo
     * @param userId  用户id
     * @return
     */
    @PostMapping("/getQuestionsAndAnswers")
    public Result<Object> getQuestionsAndAnswers(PageVo pageVo, String userId) {

        return Optional.ofNullable(iDynamicVideoService.getQuestionsAndAnswers(userId))
                .map(QuestionsAndAnswers-> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",QuestionsAndAnswers.size());
                    result.put("data", PageUtil.listToPage(pageVo,QuestionsAndAnswers));

                    return new ResultUtil<>().setData(result,"获取问答数据列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
