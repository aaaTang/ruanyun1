package cn.ruanyun.backInterface.modules.business.color.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.color.entity.Color;
import cn.ruanyun.backInterface.modules.business.color.service.IcolorService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "商品颜色管理接口")
@RequestMapping("/ruanyun/color")
@Transactional
public class ColorController {

    @Autowired
    private IcolorService icolorService;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 插入颜色数据
     * @param color
     * @return
     */
    @PostMapping("/insertColor")
    public Result<Object> insertColor(Color color) {

        color.setCreateBy(securityUtil.getCurrUser().getId());

        ThreadPoolUtil.getPool().execute(() -> icolorService.insertColorCategory(color));

        return new ResultUtil<>().setSuccessMsg("插入成功！");
    }


    /**
     * 删除颜色
     * @param id
     * @return
     */
    @PostMapping("/deleteColor")
    public Result<Object> deleteColor(String id) {

        icolorService.deleteColorCategory(id);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }

    /**
     * 更新颜色
     * @param color
     * @return
     */
    @PostMapping("/updateColor")
    public Result<Object> updateColor(Color color) {
        icolorService.updateColorCategory(color);
        return new ResultUtil<>().setSuccessMsg("更新成功！");
    }

    /**
     * 获取颜色列表数据
     * @param pid
     * @return
     */
    @PostMapping("/getColorList")
    public Result<Object> getColorList(String pid) {
        if (" ".equals(pid) || "".equals(pid)) {
            pid = null;
        }
        return Optional.ofNullable(icolorService.getColorList(pid,securityUtil.getCurrUser().getId()))
                .map(colorVOS -> new ResultUtil<>().setData(colorVOS,"获取颜色列表数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
