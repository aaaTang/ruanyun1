package cn.ruanyun.backInterface.modules.business.advertising.serviceimpl;

import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.modules.business.advertising.VO.AppAdvertisingListVO;
import cn.ruanyun.backInterface.modules.business.advertising.mapper.AdvertisingMapper;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 广告管理接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAdvertisingServiceImpl extends ServiceImpl<AdvertisingMapper, Advertising> implements IAdvertisingService {


    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void insertOrderUpdateAdvertising(Advertising advertising) {

        if (ToolUtil.isEmpty(advertising.getCreateBy())) {

            advertising.setCreateBy(securityUtil.getCurrUser().getId());
        } else {

            advertising.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(advertising)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeAdvertising(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }


    /**
     * App查询广告数据列表
     *
     * @param advertisingType     1.开屏,  2.轮播
     * @param advertisingJumpType 1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页
     * @return
     */
    @Override
    public List<AppAdvertisingListVO> APPgetAdvertisingList(String advertisingType, String advertisingJumpType) {
        String jumpType = null;
        if (ToolUtil.isNotEmpty(advertisingJumpType)) {
            if (advertisingJumpType.equals("3")) {
                jumpType = "3,4";
            } else {
                jumpType = advertisingJumpType;
            }
        }
        List<Advertising> list = this.list(new QueryWrapper<Advertising>().lambda()
              .eq(EmptyUtil.isNotEmpty(advertisingType), Advertising::getAdvertisingType, advertisingType)

              .inSql(ToolUtil.isNotEmpty(jumpType), Advertising::getAdvertisingJumpType, jumpType)
        );

        List<AppAdvertisingListVO> appAdvertisingListVOS = list.parallelStream().map(advertising -> {
            AppAdvertisingListVO advertisingListVO = new AppAdvertisingListVO();
            ToolUtil.copyProperties(advertising, advertisingListVO);
            return advertisingListVO;
        }).collect(Collectors.toList());

        return appAdvertisingListVOS;

    }

    /**
     * 后端查询广告数据列表
     *
     * @param advertisingType     1.开屏,  2.轮播
     * @param advertisingJumpType 1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页
     * @return
     */
    @Override
    public List<Advertising> BackGetAdvertisingList(String advertisingType, String advertisingJumpType) {

        return this.list(new QueryWrapper<Advertising>().lambda()
                .eq(EmptyUtil.isNotEmpty(advertisingType), Advertising::getAdvertisingType, advertisingType)
                .eq(EmptyUtil.isNotEmpty(advertisingJumpType), Advertising::getAdvertisingJumpType, advertisingJumpType)
                .orderByDesc(Advertising::getCreateTime)
        );

    }
}