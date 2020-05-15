package cn.ruanyun.backInterface.modules.business.goodCategory.controller;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "商品分类管理接口")
@RequestMapping("/ruanyun/goodCategory")
@Transactional
public class GoodCategoryController {

    @Autowired
    private IGoodCategoryService iGoodCategoryService;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 插入分类数据
     * @param goodCategory
     * @return
     */
    @PostMapping("/insertGoodCategory")
    public Result<Object> insertGoodCategory(GoodCategory goodCategory) {

        try {

            iGoodCategoryService.insertGoodCategory(goodCategory);
            return new ResultUtil<>().setSuccessMsg("插入成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, "插入失败！");
        }
    }


    /**
     * 删除分类
     * @param id
     * @return
     */
    @PostMapping("/deleteGoodCategory")
    public Result<Object> deleteGoodCategory(String id) {

        iGoodCategoryService.deleteGoodCategory(id);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }


    /**
     * 更新分类
     * @param goodCategory
     * @return
     */
    @PostMapping("/updateGoodCategory")
    public Result<Object> updateGoodCategory(GoodCategory goodCategory) {
        iGoodCategoryService.updateGoodCategory(goodCategory);
        return new ResultUtil<>().setSuccessMsg("更新成功！");
    }


    /**
     * 获取分类数据
     * @param pid
     * @return
     */
    @PostMapping("/getGoodCategoryList")
    public Result<Object> getGoodCategoryList(String pid) {
        if (" ".equals(pid) || "".equals(pid)) {
            pid = null;
        }
        return Optional.ofNullable(iGoodCategoryService.getGoodCategoryList(pid))
                .map(goodCategoryVOS -> new ResultUtil<>().setData(goodCategoryVOS,"获取分类数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * app获取分类列表
     * @return
     */
    @PostMapping("/getAppGoodCategoryList")
    public Result<Object> getAppGoodCategoryList() {
        return Optional.ofNullable(iGoodCategoryService.getAppGoodCategoryList())
                .map(goodCategoryVOS -> new ResultUtil<>().setData(goodCategoryVOS,"获取分类数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 按分类获取商家列表
     * @return
     */
    @PostMapping(value = "/getCategoryShop")
    public Result<Object> getCategoryShop(PageVo pageVo, String classId,String areaId){

        return Optional.ofNullable(iGoodCategoryService.getCategoryShop(classId,areaId))
                .map(categoryShop -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",  categoryShop.size());
                    result.put("data", PageUtil.listToPage(pageVo,categoryShop));
                    return new ResultUtil<>().setData(result,"按分类获取商家列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
