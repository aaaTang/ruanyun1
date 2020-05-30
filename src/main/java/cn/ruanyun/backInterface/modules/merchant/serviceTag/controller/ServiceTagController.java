package cn.ruanyun.backInterface.modules.merchant.serviceTag.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.DTO.ServiceTagDTO;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.pojo.ServiceTag;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.service.IServiceTagService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 优质服务标签管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/serviceTag")
@Transactional
public class ServiceTagController {

    @Autowired
    private IServiceTagService iServiceTagService;


   /**
     * 更新或者插入数据
     * @param serviceTag
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateServiceTag")
    public Result<Object> insertOrderUpdateServiceTag(ServiceTag serviceTag){

        try {

            iServiceTagService.insertOrderUpdateServiceTag(serviceTag);
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
    @PostMapping(value = "/removeServiceTag")
    public Result<Object> removeServiceTag(String ids){

        try {

            iServiceTagService.removeServiceTag(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    @PostMapping("/getServiceTag")
    @ApiOperation(value = "获取优质服务标签列表")
    public Result<Object> getServiceTag(PageVo pageVo, ServiceTagDTO serviceTagDTO) {

        return Optional.ofNullable(iServiceTagService.getServiceTag(serviceTagDTO))
                .map(serviceTag -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",serviceTag.size());
                    result.put("data", PageUtil.listToPage(pageVo,serviceTag));
                    return new ResultUtil<>().setData(result,"获取优质服务标签列表！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
