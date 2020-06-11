package cn.ruanyun.backInterface.modules.business.area.controller;

import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import com.google.common.collect.Maps;
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
 * 城市管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/area")
@Transactional
public class AreaController {

    @Autowired
    private IAreaService iAreaService;



   /**
     * 更新或者插入数据
     * @param area
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateArea")
    public Result<Object> insertOrderUpdateArea(Area area){
        try {
            iAreaService.insertOrderUpdateArea(area);
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
    @PostMapping(value = "/removeArea")
    public Result<Object> removeArea(String ids){
        try {
            iAreaService.removeArea(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取后台管理系统城市列表
     * @param pid
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackAreaList")
    public Result<Object> getBackAreaList(String pid, PageVo pageVo) {

        return Optional.ofNullable(iAreaService.getBackAreaList(pid))
                .map(backAreaVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backAreaVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backAreaVOS));

                    return new ResultUtil<>().setData(result, "获取区域数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 获取热门城市
     */
    @PostMapping("/getAppHotAreaList")
    public Result<Object> getAppHotAreaList() {
        return Optional.ofNullable(iAreaService.getAppHotAreaList())
                .map(appHotAreaList -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data", appHotAreaList);
                    return new ResultUtil<>().setData(result, "获取热门城市成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }




    /**
     * 获取app区域数据
     * @return
     */
    @PostMapping("/getAppAreaList")
    public Result<Object> getAppAreaList(PageVo pageVo) {

        return Optional.ofNullable(iAreaService.getAppAreaList())
                .map(appAreaList -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", appAreaList.size());
                    result .put("data", PageUtil.listToPage(pageVo, appAreaList));
                    return new ResultUtil<>().setData(result, "获取app区域数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }



}
