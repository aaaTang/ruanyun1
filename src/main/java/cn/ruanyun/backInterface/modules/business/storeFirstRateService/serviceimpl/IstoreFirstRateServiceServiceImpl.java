package cn.ruanyun.backInterface.modules.business.storeFirstRateService.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.mapper.StoreFirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 商家优质服务接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IstoreFirstRateServiceServiceImpl extends ServiceImpl<StoreFirstRateServiceMapper, StoreFirstRateService> implements IstoreFirstRateServiceService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFirstRateServiceService firstRateServiceService;

    @Override
    public void insertOrderUpdateStoreFirstRateService(StoreFirstRateService storeFirstRateService) {

        if (ToolUtil.isEmpty(storeFirstRateService.getCreateBy())) {

            storeFirstRateService.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            storeFirstRateService.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeFirstRateService)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeStoreFirstRateService(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public Result<Object> checkStoreFirstRate(StoreFirstRateService storeFirstRateService) {

       return Optional.ofNullable(this.getById(storeFirstRateService.getId())).map(storeFirstRateServiceUpdate -> {

            if (ObjectUtil.equal(CheckEnum.CHECK_SUCCESS, storeFirstRateService.getCheckStatus())) {

                //1. 移除该商家之前申请过的审核通过的优质服务列表
                this.remove(Wrappers.<StoreFirstRateService>lambdaQuery().eq(StoreFirstRateService::getCheckStatus, CheckEnum.CHECK_SUCCESS)
                .eq(StoreFirstRateService::getCreateBy, storeFirstRateServiceUpdate.getCreateBy()));

                //2. 分值加一
                Optional.ofNullable(userService.getById(storeFirstRateService.getCreateBy())).ifPresent(user -> {

                    user.setScore(user.getScore() + 1);
                    userService.updateById(user);
                });

            }

            //2. 赋值操作
            ToolUtil.copyProperties(storeFirstRateService, storeFirstRateServiceUpdate);
            this.updateById(storeFirstRateServiceUpdate);

            return new ResultUtil<>().setSuccessMsg("审核成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(202, "该记录不存在！"));

    }

    @Override
    public List<String> getStoreFirstRateService(String storeId) {

        return Optional.ofNullable(this.getOne(Wrappers.<StoreFirstRateService>lambdaQuery()
        .eq(StoreFirstRateService::getCheckStatus, CheckEnum.CHECK_SUCCESS)
        .eq(StoreFirstRateService::getCreateBy, storeId)))
        .map(storeFirstRateService -> firstRateServiceService.getFirstRateName(storeFirstRateService.getFirstRateServiceIds()))
        .orElse(null);
    }

}