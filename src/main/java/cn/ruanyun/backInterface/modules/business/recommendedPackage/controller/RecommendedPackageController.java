package cn.ruanyun.backInterface.modules.business.recommendedPackage.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.pojo.RecommendedPackage;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.service.IRecommendedPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 推荐商品和套餐管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/recommendedPackage")
@Transactional
public class RecommendedPackageController {

    @Autowired
    private IRecommendedPackageService iRecommendedPackageService;


   /**
     * 更新或者插入数据
     * @param recommendedPackage
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateRecommendedPackage")
    public Result<Object> insertOrderUpdateRecommendedPackage(RecommendedPackage recommendedPackage){

        try {

            iRecommendedPackageService.insertOrderUpdateRecommendedPackage(recommendedPackage);
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
    @PostMapping(value = "/removeRecommendedPackage")
    public Result<Object> removeRecommendedPackage(String ids){

        try {

            iRecommendedPackageService.removeRecommendedPackage(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
