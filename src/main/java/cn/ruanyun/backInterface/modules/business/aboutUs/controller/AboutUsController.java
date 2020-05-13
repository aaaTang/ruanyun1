package cn.ruanyun.backInterface.modules.business.aboutUs.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.aboutUs.pojo.AboutUs;
import cn.ruanyun.backInterface.modules.business.aboutUs.service.IAboutUsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 关于我们管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/aboutUs")
@Transactional
public class AboutUsController {

    @Autowired
    private IAboutUsService iAboutUsService;


   /**
     * 更新或者插入数据
     * @param aboutUs
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateAboutUs")
    public Result<Object> insertOrderUpdateAboutUs(AboutUs aboutUs){

        try {

            iAboutUsService.insertOrderUpdateAboutUs(aboutUs);
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
    @PostMapping(value = "/removeAboutUs")
    public Result<Object> removeAboutUs(String ids){

        try {

            iAboutUsService.removeAboutUs(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 查询
     * @return
     */
    @PostMapping(value = "/getAboutUs")
    public Result<Object> getAboutUs(){
        try {
            return new ResultUtil<>().setData(iAboutUsService.getAboutUs());
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


}
