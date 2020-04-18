package cn.ruanyun.backInterface.modules.business.comment.controller;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Common;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommonService;
import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 评价管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/common")
@Transactional
public class CommonController {

    @Autowired
    private ICommonService iCommonService;


   /**
     * 更新或者插入数据
     * @param common
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateCommon")
    public Result<Object> insertOrderUpdateCommon(Common common){

        try {
            iCommonService.insertOrderUpdateCommon(common);
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
    @PostMapping(value = "/removeCommon")
    public Result<Object> removeCommon(String ids){
        try {
            iCommonService.removeCommon(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * app 商品获取评论 商家获取评论
     * @param common
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppGoodList")
    public Result<Object> getAppGoodList(Common common, PageVo pageVo) {
        return Optional.ofNullable(iCommonService.getCommonList(common))
                .map(commonListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",commonListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,commonListVOS));
                    return new ResultUtil<>().setData(result,"获取商品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
