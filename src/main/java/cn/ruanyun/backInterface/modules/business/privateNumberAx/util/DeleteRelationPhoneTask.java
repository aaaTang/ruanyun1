package cn.ruanyun.backInterface.modules.business.privateNumberAx.util;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.privateNumber.service.IPrivateNumberService;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.service.IPrivateNumberAxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-06-10 00:27
 **/
@Configuration
@EnableScheduling
@Slf4j
public class DeleteRelationPhoneTask {


    @Autowired
    private IPrivateNumberAxService privateNumberAxService;

    @Autowired
    private IPrivateNumberService privateNumberService;

    @Scheduled(cron = "0 0 1 * * ?")
    private void removePhone() {

        Optional.ofNullable(ToolUtil.setListToNul(privateNumberAxService.list()))
                .ifPresent(privateNumberAxes -> privateNumberAxes.parallelStream().forEach(privateNumberAx -> {

                    //1 接触绑定
                    AXBPrivateNumberUtils.axbUnbindNumber(null, privateNumberAx.getSubscriptionId());
                    log.info("接触绑定成功");

                    //2. 更新虚拟号使用关系
                    Optional.ofNullable(privateNumberService.getById(privateNumberAx.getPrivateNumberId()))
                            .ifPresent(privateNumber -> {

                                privateNumber.setBound(BooleanTypeEnum.NO);
                                privateNumberService.updateById(privateNumber);
                            });

                    log.info("更新虚拟号使用关系成功");

                    //2.删除虚拟号段关系表
                    privateNumberAxService.removeById(privateNumberAx.getId());
                    log.info("删除虚拟号段关系表成功");

                }));

    }

}
