package cn.ruanyun.backInterface.modules.business.harvestAddress.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:33
 **/
@Data
@Accessors(chain = true)
public class HarvestAddressVO {

    private String id;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收获手机号
     */
    private String phone;

    /**
     * 收获地址
     */
    private String address;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 是否是默认收货地址
     */
    private Integer defaultAddress;


}
