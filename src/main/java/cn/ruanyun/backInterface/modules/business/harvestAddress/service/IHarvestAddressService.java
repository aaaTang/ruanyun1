package cn.ruanyun.backInterface.modules.business.harvestAddress.service;


import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 收获地址接口
 * @author fei
 */
public interface IHarvestAddressService extends IService<HarvestAddress> {

    /**
     * 插入地址
     * @param harvestAddress
     */
    void insertAddress(HarvestAddress harvestAddress);


    /**
     * 删除地址
     * @param id
     */
    void deleteAddress(String id);


    /**
     * 修改地址
     * @param harvestAddress
     */
    void updateAddress(HarvestAddress harvestAddress);


    /**
     * 获取我的收获地址详情
     * @param id
     * @return
     */
    HarvestAddressVO getAddressDetail(String id);

    /**
     * 获取我的收货地址列表
     * @return
     */
    List<HarvestAddressVO> getAddressList();

    /**
     * 获取我默认的收获地址
     * @return
     */
    HarvestAddressVO getMyDefaultAddress();

}