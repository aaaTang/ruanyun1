package cn.ruanyun.backInterface.modules.business.harvestAddress.serviceimpl;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import cn.ruanyun.backInterface.modules.business.harvestAddress.mapper.HarvestAddressMapper;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 收获地址接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IHarvestAddressServiceImpl extends ServiceImpl<HarvestAddressMapper, HarvestAddress> implements IHarvestAddressService {

    @Autowired
    private SecurityUtil securityUtil;



    /**
     * 插入地址
     *
     * @param harvestAddress
     */
    @Override
    public void insertAddress(HarvestAddress harvestAddress) {

        harvestAddress.setCreateBy(securityUtil.getCurrUser().getId());

        CompletableFuture.runAsync(() -> this.save(harvestAddress));
    }

    /**
     * 删除地址
     *
     * @param id
     */
    @Override
    public void deleteAddress(String id) {

        CompletableFuture.runAsync(() -> this.removeById(id));
    }

    /**
     * 修改地址
     *
     * @param harvestAddress
     */
    @Override
    public void updateAddress(HarvestAddress harvestAddress) {

        CompletableFuture.runAsync(() -> {
            HarvestAddress harvestAddressOld = this.getById(harvestAddress.getId());
            ToolUtil.copyProperties(harvestAddress,harvestAddressOld);
            this.updateById(harvestAddressOld);
        });
    }

    /**
     * 获取我的收获地址详情
     *
     * @param id
     * @return
     */
    @Override
    public HarvestAddressVO getAddressDetail(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(harvestAddress -> {
                    HarvestAddressVO harvestAddressVO = new HarvestAddressVO();
                    ToolUtil.copyProperties(harvestAddress,harvestAddressVO);
                    return harvestAddressVO;
                }).orElse(null);
    }

    /**
     * 获取我的收货地址列表
     *
     * @return
     */
    @Override
    public List<HarvestAddressVO> getAddressList() {

        String currentUser = securityUtil.getCurrUser().getId();
        //1.获取封装之前的数据
        CompletableFuture<Optional<List<HarvestAddress>>> harvestAddressLists = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(this.list(Wrappers.<HarvestAddress>lambdaQuery()
                .eq(HarvestAddress::getCreateBy,currentUser)
                .orderByDesc(HarvestAddress::getCreateTime))));

        //2.获取封装之后的数据
        CompletableFuture<List<HarvestAddressVO>> harvestAddressVOLists = harvestAddressLists.thenApplyAsync(harvestAddresses ->
                harvestAddresses.map(harvestAddresses1 -> harvestAddresses1.parallelStream().flatMap(harvestAddress ->
                        Stream.of(getAddressDetail(harvestAddress.getId()))).collect(Collectors.toList()))
                        .orElse(null));

        return harvestAddressVOLists.join();

    }

    /**
     * 获取我默认的收获地址
     *
     * @return
     */
    @Override
    public HarvestAddressVO getMyDefaultAddress() {
        return Optional.ofNullable(this.getOne(Wrappers.<HarvestAddress>lambdaQuery().eq(HarvestAddress::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(HarvestAddress::getDefaultAddress, CommonConstant.YES)))
                .map(harvestAddresses -> getAddressDetail(harvestAddresses.getId()))
                .orElse(null);
    }
}