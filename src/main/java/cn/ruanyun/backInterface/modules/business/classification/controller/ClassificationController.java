package cn.ruanyun.backInterface.modules.business.classification.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;


/**
 * @author fei
 * 分类管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/classification")
@Transactional
public class ClassificationController {

    @Autowired
    private IClassificationService iClassificationService;


   /**
     * 更新或者插入数据
     * @param classification
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateClassification")
    public Result<Object> insertOrderUpdateClassification(Classification classification){
        try {
            iClassificationService.insertOrderUpdateClassification(classification);
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
    @PostMapping(value = "/removeClassification")
    public Result<Object> removeClassification(String ids){
        try {
            iClassificationService.removeClassification(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取APP分类集合一级加二级
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppCategoryList")
    public Result<Object> getAppCategoryList(PageVo pageVo){

        return Optional.ofNullable(iClassificationService.getAppCategoryList())
                .map(appCategoryListVOS -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", appCategoryListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, appCategoryListVOS));

                    return new ResultUtil<>().setData(result, "获取app分类列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 按一级分类ID查询二级分类
     * @param pageVo
     * @param ids
     * @return
     */
    @PostMapping("/getSecondLevelCategory")
    public Result<Object> getSecondLevelCategory(PageVo pageVo,String ids){

        return Optional.ofNullable(iClassificationService.getSecondLevelCategory(ids))
                .map(appCategoryVO -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", appCategoryVO.size());
                    result.put("data", PageUtil.listToPage(pageVo, appCategoryVO));

                    return new ResultUtil<>().setData(result, "获取app二级分类列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }



    /**
     * 后端查询一级及二级
     */
    @PostMapping("/getCategoryList")
    public Result<Object> getCategoryList(){

        return Optional.ofNullable(iClassificationService.getCategoryList())
                .map(CategoryListVOS -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", CategoryListVOS.size());
                    result.put("data", CategoryListVOS);

                    return new ResultUtil<>().setData(result, "获取分类数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 后端按ID查询详情
     */
    @PostMapping("/getCategory")
    public Result<Classification> getCategoryList(String ids){
        Classification classification = iClassificationService.getById(ids);
        return new ResultUtil<Classification>().setData(classification);
    }



}
