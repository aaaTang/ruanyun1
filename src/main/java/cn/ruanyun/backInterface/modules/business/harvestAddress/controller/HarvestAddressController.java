package cn.ruanyun.backInterface.modules.business.harvestAddress.controller;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "收获地址管理接口")
@RequestMapping("/ruanyun/harvestAddress")
@Transactional
public class HarvestAddressController {

    @Autowired
    private IHarvestAddressService iHarvestAddressService;


    /**
     * 插入默认地址
     * @param harvestAddress
     * @return
     */
    @PostMapping("/insertAddress")
    public Result<Object> insertAddress(HarvestAddress harvestAddress) {

        if(harvestAddress.getConsignee().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()==0){
            //不能重复添加默认地址
            if (CommonConstant.YES.equals(harvestAddress.getDefaultAddress())) {
                return Optional.ofNullable(iHarvestAddressService.getMyDefaultAddress())
                        .map(harvestAddressVO -> {

                            iHarvestAddressService.insertAddress(harvestAddress);

                            //把默认地址改成普通地址
                            HarvestAddress harvestAdd = new HarvestAddress();
                            harvestAdd.setId(harvestAddressVO.getId());
                            harvestAdd.setDefaultAddress(CommonConstant.NO);
                            iHarvestAddressService.updateAddress(harvestAdd);

                            return new ResultUtil<>().setSuccessMsg("新增成功！");
                        }).orElseGet(() -> {
                            iHarvestAddressService.insertAddress(harvestAddress);
                            return new ResultUtil<>().setSuccessMsg("新增成功！");
                        });

            }

            iHarvestAddressService.insertAddress(harvestAddress);
            return new ResultUtil<>().setErrorMsg(201,"新增失败！");
        }else {
            return new ResultUtil<>().setErrorMsg(201,"收货人名称不能保护特殊字符！");
        }

    }


    /**
     * 移除收获地址
     * @param id
     * @return
     */
    @PostMapping("/deleteAddress")
    public Result<Object> deleteAddress(String id) {

        return Optional.ofNullable(iHarvestAddressService.getById(id))
                .map(harvestAddress -> {
                    iHarvestAddressService.deleteAddress(id);
                    return new ResultUtil<>().setSuccessMsg("删除成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"不存在此地址！"));
    }


    /**
     * 更新地址
     * @param harvestAddress
     * @return
     */
    @PostMapping("/updateAddress")
    public Result<Object> updateAddress(HarvestAddress harvestAddress) {

        //不能重复添加默认地址
        if (CommonConstant.YES.equals(harvestAddress.getDefaultAddress())) {
            return Optional.ofNullable(iHarvestAddressService.getMyDefaultAddress())
                    .map(harvestAddressVO -> {
                        //把默认地址改成普通地址
                        HarvestAddress harvestAdd = new HarvestAddress();
                        harvestAdd.setId(harvestAddressVO.getId());
                        harvestAdd.setDefaultAddress(CommonConstant.NO);
                        iHarvestAddressService.updateAddress(harvestAdd);

                        iHarvestAddressService.updateAddress(harvestAddress);
                        return new ResultUtil<>().setSuccessMsg("更新成功！");
                    }).orElseGet(() -> {
                        iHarvestAddressService.updateAddress(harvestAddress);
                        return new ResultUtil<>().setSuccessMsg("更新成功！");
                    });
        }

        iHarvestAddressService.updateAddress(harvestAddress);
        return new ResultUtil<>().setSuccessMsg("更新成功！");
    }


    /**
     * 获取详情
     * @param id
     * @return
     */
    @PostMapping("/getAddressDetail")
    public Result<Object> getAddressDetail(String id) {

        return new ResultUtil<>().setData(iHarvestAddressService.getAddressDetail(id),"获取详情成功！");
    }


    /**
     * 获取我的收货地址列表
     * @return
     */
    @PostMapping("/getAddressList")
    public Result<Object> getAddressList() {

        return Optional.ofNullable(iHarvestAddressService.getAddressList())
                .map(harvestAddressVOS -> new ResultUtil<>().setData(harvestAddressVOS,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取我的默认收获地址
     * @return
     */
    @PostMapping("/getMyDefaultAddress")
    public Result<Object> getMyDefaultAddress() {

        return Optional.ofNullable(iHarvestAddressService.getMyDefaultAddress())
                .map(harvestAddressVO -> new ResultUtil<>().setData(harvestAddressVO,"获取我的默认收获地址成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无默认收获地址！"));
    }



}
