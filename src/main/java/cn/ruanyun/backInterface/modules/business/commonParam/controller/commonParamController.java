package cn.ruanyun.backInterface.modules.business.commonParam.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.commonParam.pojo.commonParam;
import cn.ruanyun.backInterface.modules.business.commonParam.service.IcommonParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 公众参数管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/commonParam")
@Transactional
public class commonParamController {

    @Autowired
    private IcommonParamService icommonParamService;

    /**
     * 插入或者修改公众参数
     * @param commonParam 实体
     * @return Object
     */
    @PostMapping("/insertOrUpdateCommonParam")
    private Result<Object> insertOrUpdateCommonParam(commonParam commonParam) {

        try {

            icommonParamService.insertOrUpdateCommonParam(commonParam);
            return new ResultUtil<>().setSuccessMsg("添加或者修改成功！");
        }catch (RuanyunException ruanyunException) {

            throw new RuntimeException(ruanyunException.getMessage());
        }
    }

    /**
     * 获取数据
     * @return Object
     */
    @PostMapping("/getCommonParamVo")
    public Result<Object> getCommonParamVo() {

        return Optional.ofNullable(icommonParamService.getCommonParamVo())
                .map(commonParamVo -> new ResultUtil<>().setData(commonParamVo, "获取公众参数成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }
}
