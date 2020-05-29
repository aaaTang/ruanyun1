package cn.ruanyun.backInterface.modules.business.withdrawDeposit.serviceimpl;

import cn.ruanyun.backInterface.common.pay.dto.TransferDto;
import cn.ruanyun.backInterface.common.pay.service.IPayService;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.DTO.TransferMoneyDTO;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.mapper.WithdrawDepositMapper;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.pojo.WithdrawDeposit;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.service.IWithdrawDepositService;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;

import javax.annotation.Resource;


/**
 * 提现管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IWithdrawDepositServiceImpl extends ServiceImpl<WithdrawDepositMapper, WithdrawDeposit> implements IWithdrawDepositService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private UserMapper userMapper;
       @Autowired
       private IPayService payService;

       @Override
       public Result<Object> insertWithdrawDeposit(WithdrawDeposit withdrawDeposit) {

           User user = userMapper.selectById(securityUtil.getCurrUser().getId());

           if(ToolUtil.isNotEmpty(user)){
               //查询用户的余额够不够申请
               int a = user.getBalance().compareTo(withdrawDeposit.getMoney());

               if(a==1||a==0){

                   withdrawDeposit.setCreateBy(securityUtil.getCurrUser().getId());
                   return new ResultUtil<>().setData(this.save(withdrawDeposit),"申请提现成功！");

               }else {
                   return new ResultUtil<>().setErrorMsg(201,"余额不足，申请提现失败！");
               }


           }else {
               return new ResultUtil<>().setErrorMsg(201,"用户信息错误！");
           }


       }


    @Override
    public Result<Object> UpdateWithdrawDeposit(WithdrawDeposit withdrawDeposit) {

        WithdrawDeposit withdraw = this.getById(withdrawDeposit.getId());

        User user = new User();
        if(ToolUtil.isNotEmpty(withdraw)){
          user = userMapper.selectById(withdraw.getCreateBy());
        }

        if(ToolUtil.isNotEmpty(user)){
            //查询用户的余额够不够申请
            int a = user.getBalance().compareTo(withdraw.getMoney());

            if(a==1||a==0){

                withdraw.setUpdateBy(securityUtil.getCurrUser().getId());
                withdraw.setType(withdrawDeposit.getType());
                return new ResultUtil<>().setData(this.updateById(withdraw),"处理提现成功！");

                //TODO:://转账
            }else {

                withdraw.setUpdateBy(securityUtil.getCurrUser().getId());
                withdraw.setType(0);
                withdraw.setContent("用户余额不足!");
                return new ResultUtil<>().setData(this.updateById(withdraw),"用户余额不足！");

            }

        }else {
            return new ResultUtil<>().setErrorMsg(201,"用户信息错误！");
        }


    }



      @Override
      public void removeWithdrawDeposit(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 后端获取提现记录
     * @param withdrawDeposit
     * @return
     */
      @Override
      public List PcGetWithdrawDeposit(WithdrawDeposit withdrawDeposit) {

            List<WithdrawDeposit> withdrawDepositList = this.list(new QueryWrapper<WithdrawDeposit>().lambda()
                .eq((ToolUtil.isNotEmpty(withdrawDeposit.getPayTypeEnum())),WithdrawDeposit::getPayTypeEnum,withdrawDeposit.getPayTypeEnum())
                .eq((ToolUtil.isNotEmpty(withdrawDeposit.getType())),WithdrawDeposit::getType,withdrawDeposit.getType())
                .like((ToolUtil.isNotEmpty(withdrawDeposit.getMobile())),WithdrawDeposit::getMobile,withdrawDeposit.getMobile())
                .like((ToolUtil.isNotEmpty(withdrawDeposit.getWithdrawUser())),WithdrawDeposit::getWithdrawUser,withdrawDeposit.getWithdrawUser())
            );

           return withdrawDepositList;
      }



}