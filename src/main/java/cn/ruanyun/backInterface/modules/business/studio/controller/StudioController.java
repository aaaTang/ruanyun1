package cn.ruanyun.backInterface.modules.business.studio.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.studio.dto.StudioDto;
import cn.ruanyun.backInterface.modules.business.studio.pojo.Studio;
import cn.ruanyun.backInterface.modules.business.studio.service.IstudioService;
import cn.ruanyun.backInterface.modules.business.studio.vo.StudioListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fei
 * 工作室管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/studio")
@Transactional
@Api(tags = "工作室管理接口")
public class StudioController {

    @Autowired
    private IstudioService istudioService;


    @PostMapping(value = "/insertOrderUpdateStudio")
    @ApiOperation("添加邀请数据")
    public Result<Object> insertOrderUpdateStudio(Studio studio){

        try {

            istudioService.insertOrderUpdateStudio(studio);
            return new ResultUtil<>().setSuccessMsg("添加邀请数据成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping(value = "/removeStudio")
    @ApiOperation("移除成员信息")
    public Result<Object> removeStudio(String ids){

        try {

            istudioService.removeStudio(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping("/resolveInvite")
    @ApiOperation("处理邀请信息")
    public Result<Object> resolveInvite(Studio studio) {

        return istudioService.resolveInvite(studio);
    }


    @PostMapping("/getStudioList")
    @ApiOperation(value = "获取团队信息")
    public Result<DataVo<StudioListVo>> getStudioList(PageVo pageVo, StudioDto studioDto) {

        return istudioService.getStudioList(pageVo, studioDto);
    }

}
