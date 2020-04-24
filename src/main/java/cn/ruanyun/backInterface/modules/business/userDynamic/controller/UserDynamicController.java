package cn.ruanyun.backInterface.modules.business.userDynamic.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userDynamic.DTO.UserDynamicDTO;
import cn.ruanyun.backInterface.modules.business.userDynamic.pojo.UserDynamic;
import cn.ruanyun.backInterface.modules.business.userDynamic.service.IUserDynamicService;
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
 * 用户动态管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userDynamic")
@Transactional
public class UserDynamicController {

    @Autowired
    private IUserDynamicService iUserDynamicService;


   /**
     * 更新或者插入数据
     * @param userDynamic
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserDynamic")

    public Result<Object> insertOrderUpdateUserDynamic(UserDynamic userDynamic){

        try {
            iUserDynamicService.insertOrderUpdateUserDynamic(userDynamic);
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
    @PostMapping(value = "/removeUserDynamic")
    public Result<Object> removeUserDynamic(String ids){

        try {

            iUserDynamicService.removeUserDynamic(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * App查询用户动态
     * @return
     */
    @PostMapping(value = "/getUserDynamic")
    public Result<Object> getUserDynamic(PageVo pageVo, UserDynamicDTO userDynamicDTO){

        return Optional.ofNullable(iUserDynamicService.getUserDynamic(userDynamicDTO))
              .map(userDynamic -> {
                  Map<String,Object> result = Maps.newHashMap();
                  result.put("size",userDynamic.size());
                  result.put("data", PageUtil.listToPage(pageVo,userDynamic));
                  return new ResultUtil<>().setData(result,"App查询用户动态列表成功！");
              })
        .orElse(new ResultUtil<>().setErrorMsg("暂无数据"));
    }



}
