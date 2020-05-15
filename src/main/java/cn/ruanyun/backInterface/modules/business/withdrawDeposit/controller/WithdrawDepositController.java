package cn.ruanyun.backInterface.modules.business.withdrawDeposit.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.DTO.PcOrderDTO;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.pojo.WithdrawDeposit;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.service.IWithdrawDepositService;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 提现管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/withdrawDeposit")
@Transactional
public class WithdrawDepositController {

    @Autowired
    private IWithdrawDepositService iWithdrawDepositService;


   /**
     * 插入数据
     * @param withdrawDeposit
     * @return
    */
    @PostMapping(value = "/insertWithdrawDeposit")
    public Result<Object> insertWithdrawDeposit(WithdrawDeposit withdrawDeposit){
       return  iWithdrawDepositService.insertWithdrawDeposit(withdrawDeposit);
    }


    /**
     * 后台处理申请
     * @param withdrawDeposit
     * @return
     */
    @PostMapping(value = "/UpdateWithdrawDeposit")
    public Result<Object> UpdateWithdrawDeposit(WithdrawDeposit withdrawDeposit){
        return  iWithdrawDepositService.UpdateWithdrawDeposit(withdrawDeposit);
    }

    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeWithdrawDeposit")
    public Result<Object> removeWithdrawDeposit(String ids){

        try {

            iWithdrawDepositService.removeWithdrawDeposit(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 后端获取提现记录
     * @param pageVo
     * @return
     */
    @PostMapping("/PcGetWithdrawDeposit")
    public Result<Object> PcGetWithdrawDeposit(WithdrawDeposit withdrawDeposit, PageVo pageVo) {
        return Optional.ofNullable(iWithdrawDepositService.PcGetWithdrawDeposit(withdrawDeposit))
                .map(pcGetWithdrawDeposit -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",pcGetWithdrawDeposit.size());
                    result.put("data", PageUtil.listToPage(pageVo,pcGetWithdrawDeposit));
                    return new ResultUtil<>().setData(result,"后端获取提现记录列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
