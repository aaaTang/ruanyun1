package cn.ruanyun.backInterface.modules.business.harvestAddress.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_harvest_address")
@TableName("t_harvest_address")
@ApiModel(value = "收获地址")
public class HarvestAddress extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 城市id
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 是否是默认收货地址
     */
    private Integer defaultAddress = CommonConstant.NO;

}