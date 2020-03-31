package cn.ruanyun.backInterface.modules.business.followAttention.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhu
 * 用户关注管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/followAttention")
@Transactional
public class FollowAttentionController {

    @Autowired
    private IFollowAttentionService iFollowAttentionService;


   /**
     * 更新或者插入数据
     * @param followAttention
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateFollowAttention")
    public Result<Object> insertOrderUpdateFollowAttention(FollowAttention followAttention){

        try {

            iFollowAttentionService.insertOrderUpdateFollowAttention(followAttention);
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
    @PostMapping(value = "/removeFollowAttention")
    public Result<Object> removeFollowAttention(String ids){

        try {

            iFollowAttentionService.removeFollowAttention(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * 获取用户关注的商家列表
     * @param pageVo
     * @return
     */
    @PostMapping("/followAttentionList")
    public Result<Object> followAttentionList(PageVo pageVo) {

        return Optional.ofNullable(iFollowAttentionService.followAttentionList())
                .map(followAttention -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",followAttention.size());
                    result.put("data", PageUtil.listToPage(pageVo,followAttention));

                    return new ResultUtil<>().setData(result,"获取用户关注的商家列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取用户关注的用户列表
     * @param pageVo
     * @return
     */
    @PostMapping("/followUserList")
    public Result<Object> followUserList(PageVo pageVo) {

        return Optional.ofNullable(iFollowAttentionService.followUserList())
                .map(followAttention -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",followAttention.size());
                    result.put("data", PageUtil.listToPage(pageVo,followAttention));

                    return new ResultUtil<>().setData(result,"获取用户关注的用户列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * 获取我的粉丝
     * @param pageVo
     * @return
     */
    @PostMapping("/mefansList")
    public Result<Object> mefansList(PageVo pageVo) {

        return Optional.ofNullable(iFollowAttentionService.mefansList())
                .map(followAttention -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",followAttention.size());
                    result.put("data", PageUtil.listToPage(pageVo,followAttention));

                    return new ResultUtil<>().setData(result,"获取我的粉丝成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
