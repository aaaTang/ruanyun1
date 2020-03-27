package cn.ruanyun.backInterface.modules.business.size.controller;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.size.entity.Size;
import cn.ruanyun.backInterface.modules.business.size.service.IsizeService;
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
@Api(description = "商品尺寸管理接口")
@RequestMapping("/ruanyun/size")
@Transactional
public class SizeController {

    @Autowired
    private IsizeService isizeService;

    /**
     * 插入尺寸
     * @param size
     * @return
     */
    @PostMapping("/insertSize")
    public Result<Object> insertSize(Size size) {

        isizeService.insertSize(size);
        return new ResultUtil<>().setSuccessMsg("插入成功！");
    }


    /**&
     * 移除尺寸
     * @param id
     * @return
     */
    @PostMapping("/removeSize")
    public Result<Object> removeSize(String id) {

        isizeService.deleteSize(id);
        return new ResultUtil<>().setSuccessMsg("移除成功！");
    }

    /**
     * 更新尺寸
     * @param size
     * @return
     */
    @PostMapping("/updateSize")
    public Result<Object> updateSize(Size size) {

        isizeService.updateSize(size);
        return new ResultUtil<>().setSuccessMsg("更新成功！");
    }


    /**
     * 获取尺寸列表
     * @param pageVo
     * @return
     */
    @PostMapping("/getSizeList")
    public Result<Object> getSizeList(PageVo pageVo, String goodCategoryId) {

        return Optional.ofNullable(isizeService.getSizeList(goodCategoryId))
                .map(sizeVOS -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",sizeVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,sizeVOS));

                    return new ResultUtil<>().setData(result,"获取数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }
}
