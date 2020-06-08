package cn.ruanyun.backInterface.modules.business.userRelationship.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userRelationship.VO.AppRelationUserVO;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import cn.ruanyun.backInterface.modules.business.userVideo.DTO.UserVideoDTO;
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
 * 用户关联管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userRelationship")
@Transactional
public class UserRelationshipController {

    @Autowired
    private IUserRelationshipService iUserRelationshipService;


   /**
     * 更新或者插入数据
     * @param userRelationship
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserRelationship")
    public Result<Object> insertOrderUpdateUserRelationship(UserRelationship userRelationship){

        try {

            iUserRelationshipService.insertOrderUpdateUserRelationship(userRelationship);
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
    @PostMapping(value = "/removeUserRelationship")
    public Result<Object> removeUserRelationship(String ids){

        try {

            iUserRelationshipService.removeUserRelationship(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取我的邀请人列表数据
     * @param pageVo
     * @return
     */
    @PostMapping(value = "/getUserRelationshipListByUser")
    public Result<Object> getUserRelationshipListByUser(PageVo pageVo){

        return Optional.ofNullable(iUserRelationshipService.getUserRelationshipListByUser())
                .map(userVOList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",userVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,userVOList));
                    return new ResultUtil<>().setData(result,"获取我的邀请人列表数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }


    /**
     * 获取邀请人的订单数据列表
     * @param pageVo
     * @return
     */
    @PostMapping(value = "/getUserOrderList")
    public Result<Object> getUserOrderList(PageVo pageVo,String userId){

        return Optional.ofNullable(iUserRelationshipService.getUserOrderList(userId))
                .map( userVOList-> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",userVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,userVOList));
                    return new ResultUtil<>().setData(result,"获取邀请人的订单数据列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }










}
