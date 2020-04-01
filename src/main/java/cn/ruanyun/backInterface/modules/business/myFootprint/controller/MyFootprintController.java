package cn.ruanyun.backInterface.modules.business.myFootprint.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
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
 * 用户足迹管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/myFootprint")
@Transactional
public class MyFootprintController {

    @Autowired
    private IMyFootprintService iMyFootprintService;


   /**
     * 更新或者插入数据
     * @param myFootprint
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateMyFootprint")
    public Result<Object> insertOrderUpdateMyFootprint(MyFootprint myFootprint){

        try {

            iMyFootprintService.insertOrderUpdateMyFootprint(myFootprint);
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
    @PostMapping(value = "/removeMyFootprint")
    public Result<Object> removeMyFootprint(String ids){

        try {

            iMyFootprintService.removeMyFootprint(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取用户足迹列表
     * @param pageVo
     * @return
     */
    @PostMapping("/MyFootprintList")
    public Result<Object> MyFootprintList(PageVo pageVo) {

        return Optional.ofNullable(iMyFootprintService.MyFootprintList())
                .map(myf -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",myf.size());
                    result.put("data", PageUtil.listToPage(pageVo,myf));

                    return new ResultUtil<>().setData(result,"获取用户足迹列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
