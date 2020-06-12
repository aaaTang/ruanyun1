package cn.ruanyun.backInterface.modules.business.selectStore.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.selectStore.pojo.SelectStore;
import cn.ruanyun.backInterface.modules.business.selectStore.service.ISelectStoreService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 严选商家管理模块
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/selectStore")
@Transactional
public class SelectStoreController {

    @Autowired
    private ISelectStoreService iSelectStoreService;


   /**
     * 更新或者插入数据
     * @param selectStore
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSelectStore")
    public Result<Object> insertOrderUpdateSelectStore(SelectStore selectStore){

        try {

            iSelectStoreService.insertOrderUpdateSelectStore(selectStore);
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
    @PostMapping(value = "/removeSelectStore")
    public Result<Object> removeSelectStore(String ids){

        try {

            iSelectStoreService.removeSelectStore(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取严选商家列表
     * @param pageVo
     * @return
     */
    @PostMapping("/getSelectStoreList")
    public Result<Object> getSelectStoreList(PageVo pageVo, String areaName) {

        return Optional.ofNullable(iSelectStoreService.getSelectStoreList(areaName))
                .map(selectStoreListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",selectStoreListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,selectStoreListVOS));
                    return new ResultUtil<>().setData(result,"获取数据成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));

    }
}
