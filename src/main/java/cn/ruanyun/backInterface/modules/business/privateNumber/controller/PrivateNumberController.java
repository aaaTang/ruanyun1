package cn.ruanyun.backInterface.modules.business.privateNumber.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.privateNumber.pojo.PrivateNumber;
import cn.ruanyun.backInterface.modules.business.privateNumber.service.IPrivateNumberService;
import cn.ruanyun.backInterface.modules.business.privateNumber.vo.PrivateNumberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.tools.Tool;
import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 虚拟号管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/privateNumber")
@Transactional
public class PrivateNumberController {

    @Autowired
    private IPrivateNumberService iPrivateNumberService;

    /**
     * 添加或者修改虚拟号段
     * @param privateNumber 实体
     * @return Object
     */
    @PostMapping("/insertOrUpdatePrivateNumber")
    public Result<Object> insertOrUpdatePrivateNumber(PrivateNumber privateNumber) {

        try {

            iPrivateNumberService.insertOrUpdatePrivateNumber(privateNumber);
            return new ResultUtil<>().setSuccessMsg("新增虚拟号段成功！");
        }catch (RuanyunException e) {

            throw  new RuanyunException(e.getMsg());
        }
    }


    /**
     * 移除虚拟号段
     * @param ids ids
     * @return Object
     */
    @PostMapping("/removePrivateNumber")
    public Result<Object> removePrivateNumber(String ids) {

        try {

            iPrivateNumberService.removePrivateNumber(ids);
            return new ResultUtil<>().setSuccessMsg("移除虚拟号段成功！");
        }catch (RuanyunException e) {

            throw  new RuanyunException(e.getMsg());
        }
    }


    /**
     * 获取虚拟号段列表
     * @param pageVo 分页
     * @return Object
     */
    @PostMapping("/getPrivateNumberList")
    public Result<Object> getPrivateNumberList(PageVo pageVo) {

        DataVo<PrivateNumberVo> result = iPrivateNumberService.getPrivateNumberList(pageVo);

        return Optional.ofNullable(result.getDataResult()).map(privateNumberVos ->
                new ResultUtil<>().setData(result, "获取虚拟号段列表数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }
}
