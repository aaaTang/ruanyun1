package cn.ruanyun.backInterface.modules.business.privateNumberAx.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.service.IPrivateNumberAxService;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.vo.PrivateNumberAxVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author z
 * 华为隐私通话管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/privateNumberAx")
@Transactional
@Api(tags = "虚拟号段")
public class PrivateNumberAxController {

    @Autowired
    private IPrivateNumberAxService privateNumberAxService;

    /**
     * 获取虚拟号码
     * @param storeId 商家id
     * @return Object
     */
    @PostMapping("/getPrivateNumByStoreIdAndUseId")
    @ApiOperation("获取虚拟号码")
    public Result<Object> getPrivateNumByStoreIdAndUseId(String storeId) {

        return privateNumberAxService.getPrivateNumByStoreIdAndUseId(storeId);
    }


    /**
     * 获取虚拟号段绑定关系列表
     * @param pageVo 分页
     * @return Object
     */
    @PostMapping("/getPrivateNumberAxVoList")
    @ApiOperation("获取虚拟号段绑定关系列表")
    public Result<Object> getPrivateNumberAxVoList(PageVo pageVo) {

        DataVo<PrivateNumberAxVo> result = privateNumberAxService.getPrivateNumberAxVoList(pageVo);

        return Optional.ofNullable(result.getDataResult()).map(privateNumberAxVos ->
                new ResultUtil<>().setData(result, "获取虚拟号段绑定关系数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 解除绑定
     * @param id id
     * @return Object
     */
    @PostMapping("/unbindPrivateNum")
    public Result<Object> unbindPrivateNum(String id) {

        return privateNumberAxService.unbindPrivateNum(id);
    }
}
