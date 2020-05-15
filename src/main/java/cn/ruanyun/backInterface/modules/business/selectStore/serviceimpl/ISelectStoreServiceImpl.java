package cn.ruanyun.backInterface.modules.business.selectStore.serviceimpl;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.selectStore.VO.SelectStoreListVO;
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
    @Resource
    private GoodMapper goodMapper;

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
    public List<SelectStoreListVO> getSelectStoreList() {

        return Optional.ofNullable(ToolUtil.setListToNul(super.list(Wrappers.<SelectStore>lambdaQuery()
                .orderByDesc(SelectStore::getCreateTime))))
                .map(selectStores -> selectStores.parallelStream().flatMap(selectStore -> {

                    SelectStoreListVO selectStoreListVO = new SelectStoreListVO();
                    selectStoreListVO.setCreateTime(selectStore.getCreateTime());

                    Optional.ofNullable(userService.getById(selectStore.getUserId()))
                            .ifPresent(user ->

                                    selectStoreListVO.setAvatar((ToolUtil.isNotEmpty(user.getPic())?user.getPic().split(",")[0]:""))
                                    .setUsername(user.getShopName())
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
                }).collect(Collectors.toList()))
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