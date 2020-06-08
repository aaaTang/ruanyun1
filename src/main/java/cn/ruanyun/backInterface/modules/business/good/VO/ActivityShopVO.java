package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.modules.business.storeActivity.VO.StoreActivityVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class ActivityShopVO {

    private String id;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 轮播图
     */
    @Column(length = 1000)
    private String  pic ;

    @ApiModelProperty("店铺营业时间")
    private String openTime;

    @ApiModelProperty("优质服务")
    private List<String> firstRateService;

    @ApiModelProperty("个人简介")
    @Column(length = 500)
    private String individualResume;

    @ApiModelProperty("门店活动")
    private List<StoreActivityVO> activity;

    @ApiModelProperty(value = "到店礼")
    @Column(length = 1000)
    private String toStoreGift;
}
