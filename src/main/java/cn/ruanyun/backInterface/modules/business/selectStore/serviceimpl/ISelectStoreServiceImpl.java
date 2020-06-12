package cn.ruanyun.backInterface.modules.business.selectStore.serviceimpl;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.selectStore.vo.SelectStoreListVO;
import cn.ruanyun.backInterface.modules.business.selectStore.mapper.SelectStoreMapper;
import cn.ruanyun.backInterface.modules.business.selectStore.pojo.SelectStore;
import cn.ruanyun.backInterface.modules.business.selectStore.service.ISelectStoreService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 商品接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ISelectStoreServiceImpl extends ServiceImpl<SelectStoreMapper, SelectStore> implements ISelectStoreService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAreaService areaService;

    @Resource
    private GoodMapper goodMapper;

    @Autowired
    private IGoodCategoryService goodCategoryService;

    @Override
    public void insertOrderUpdateSelectStore(SelectStore selectStore) {
        if (ToolUtil.isEmpty(selectStore.getCreateBy())) {
            selectStore.setCreateBy(securityUtil.getCurrUser().getId());
        } else {
            selectStore.setUpdateBy(securityUtil.getCurrUser().getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(selectStore)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeSelectStore(String ids) {
        this.remove(Wrappers.<SelectStore>lambdaQuery().eq(SelectStore::getUserId,ids));
    }

    @Override
    public List<SelectStoreListVO> getSelectStoreList(String areaName) {

        return Optional.ofNullable(ToolUtil.setListToNul(super.list(Wrappers.<SelectStore>lambdaQuery()
                .orderByDesc(SelectStore::getCreateTime))))
                .map(selectStores -> {

                    List<SelectStoreListVO> selectStoreListVOS = selectStores.parallelStream().flatMap(selectStore -> {

                        SelectStoreListVO selectStoreListVO = new SelectStoreListVO();
                        selectStoreListVO.setCreateTime(selectStore.getCreateTime());

                        //判断商家类型
                        User store = userService.getById(selectStore.getUserId());
                        selectStoreListVO.setStoreType(goodCategoryService.judgeStoreType(store));

                        Optional.ofNullable(userService.getById(selectStore.getUserId()))
                                .ifPresent(user ->

                                        selectStoreListVO.setAvatar((ToolUtil.isNotEmpty(user.getPic())?user.getPic().split(",")[0]:""))
                                                .setUsername(user.getShopName())
                                                .setAreaId(user.getAreaId())
                                                .setId(user.getId())

                                );
                        List<Good> good = goodMapper.selectList(Wrappers.<Good>lambdaQuery()
                                .eq(Good::getTypeEnum, GoodTypeEnum.GOOD)
                                .eq(Good::getCreateBy,selectStore.getUserId())
                                .orderByAsc(Good::getGoodNewPrice)
                        );
                        if(good.size()>0){
                            selectStoreListVO.setLowPrice(good.get(0).getGoodNewPrice());
                        }else {
                            selectStoreListVO.setLowPrice(new BigDecimal(0));
                        }

                        return Stream.of(selectStoreListVO);
                    }).filter(Objects::nonNull).collect(Collectors.toList());

                    if (ToolUtil.isNotEmpty(areaName)) {

                        if (ToolUtil.isNotEmpty(areaService.getIdByAreaName(areaName))) {

                            selectStoreListVOS = selectStoreListVOS.parallelStream().filter(selectStoreListVO ->

                                    selectStoreListVO.getAreaId().equals(areaService.getIdByAreaName(areaName)))
                                    .collect(Collectors.toList());

                        }
                    }

                    return selectStoreListVOS;
                })
                .orElse(null);
    }

    @Override
    public Integer getSelectStore(String userId) {

        return Optional.ofNullable(super.getOne(Wrappers.<SelectStore>lambdaQuery()
                .eq(SelectStore::getUserId,userId)))
                .map(SelectStore::getStrict)
                .orElse(0);
    }


}