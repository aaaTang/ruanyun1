package cn.ruanyun.backInterface.modules.business.firstRateService.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
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
 * @author fei
 * 优质服务管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/firstRateService")
@Transactional
public class FirstRateServiceController {

    @Autowired
    private IFirstRateServiceService iFirstRateServiceService;


   /**
     * 更新或者插入数据
     * @param firstRateService
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateFirstRateService")
    public Result<Object> insertOrderUpdateFirstRateService(FirstRateService firstRateService){

        try {

            iFirstRateServiceService.insertOrderUpdateFirstRateService(firstRateService);
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
    @PostMapping(value = "/removeFirstRateService")
    public Result<Object> removeFirstRateService(String ids){

        try {

            iFirstRateServiceService.removeFirstRateService(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping("/getFirstRateService")
    @ApiOperation(value = "获取优质服务器列表")
    public Result<Object> getFirstRateService(PageVo pageVo,FirstRateService firstRateService) {

        return Optional.ofNullable(iFirstRateServiceService.getFirstRateService(firstRateService))
                .map(firstRateServiceList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",firstRateServiceList.size());
                    result.put("data", PageUtil.listToPage(pageVo,firstRateServiceList));
                    return new ResultUtil<>().setData(result,"获取优质服务器列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
