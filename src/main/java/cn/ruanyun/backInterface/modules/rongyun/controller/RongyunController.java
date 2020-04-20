package cn.ruanyun.backInterface.modules.rongyun.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 融云管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/rongyun")
@Transactional
public class RongyunController {

    @Autowired
    private IRongyunService iRongyunService;


    /**
     * 获取用户token
     * @param userId
     * @param name
     * @param portraitUri
     * @return
     * @throws RuanyunException
     */
    @PostMapping("/getToken")
    public Result<Object> getToken(String userId, String name, String portraitUri) throws RuanyunException {

        return new ResultUtil<>().setData(iRongyunService.getToken(userId, name, portraitUri),"获取token成功！");
    }


    /**
     * 判断用户在线状态
     * @param userId
     * @return
     * @throws RuanyunException
     */
    @PostMapping("/checkOnlineResult")
    public Result<Object> checkOnlineResult(String userId) throws RuanyunException {

        return new ResultUtil<>().setData(iRongyunService.checkOnlineResult(userId),"获取用户在线状态成功！");
    }

}
