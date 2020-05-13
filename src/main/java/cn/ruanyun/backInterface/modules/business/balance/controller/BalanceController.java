package cn.ruanyun.backInterface.modules.business.balance.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.balance.VO.AppBalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhu
 * 余额明细管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/balance")
@Transactional
public class BalanceController {

    @Autowired
    private IBalanceService iBalanceService;


   /**
     * 更新或者插入数据
     * @param balance
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateBalance")
    public Result<Object> insertOrderUpdateBalance(Balance balance){

        try {

            iBalanceService.insertOrderUpdateBalance(balance);
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
    @PostMapping(value = "/removeBalance")
    public Result<Object> removeBalance(String ids){

        try {

            iBalanceService.removeBalance(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * app 获取用户明细
     * @return Result<Object>
     */
    @PostMapping("/getAppBalance")
    public Result<Object> getAppBalance(PageVo pageVo) {
        return Optional.ofNullable(iBalanceService.getAppBalance(pageVo))
                .map(appBalanceVos -> {

                    DataVo<AppBalanceVO> result = new DataVo<>();
                    result.setDataResult(PageUtil.listToPage(pageVo, appBalanceVos))
                            .setTotalNumber(appBalanceVos.size());
                    return new ResultUtil<>().setData(result,"获取我的余额明细成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
