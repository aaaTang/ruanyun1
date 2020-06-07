package cn.ruanyun.backInterface.modules.business.kotlinDemo.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo;
import cn.ruanyun.backInterface.modules.business.kotlinDemo.service.IKotlinDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * kotlin栗子管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/kotlinDemo")
@Transactional
public class KotlinDemoController {

    @Autowired
    private IKotlinDemoService iKotlinDemoService;


   /**
     * 更新或者插入数据
     * @param kotlinDemo
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateKotlinDemo")
    public Result<Object> insertOrderUpdateKotlinDemo(KotlinDemo kotlinDemo){

        try {

            iKotlinDemoService.insertOrderUpdateKotlinDemo(kotlinDemo);
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
    @PostMapping(value = "/removeKotlinDemo")
    public Result<Object> removeKotlinDemo(String ids){

        try {

            iKotlinDemoService.removeKotlinDemo(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
