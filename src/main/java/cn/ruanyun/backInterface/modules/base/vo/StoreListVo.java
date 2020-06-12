package cn.ruanyun.backInterface.modules.base.vo;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AuthenticationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreListVo {


    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("门店等级 判断门店等级 1.没有等级 2.普通 3.铜牌 4.银牌 5.金牌 6.钻石")
    private Integer storeLevel;

    @ApiModelProperty("门店名称")
    private String shopName;

    @ApiModelProperty(value = "门店星级")
    private Double storeStarLevel;

    @ApiModelProperty("评价条数")
    private Integer commentNum;

    @ApiModelProperty("最低价格")
    private BigDecimal lowPrice;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty("区域id")
    private String areaId;

    @ApiModelProperty("信任标识  0无  1有 ")
    private Integer trustIdentity = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty("连锁认证 PRE_CHECK(0,待确定) MERCHANT(1,品牌商家) ALLIANCE(2,品牌联盟) ")
    private AuthenticationTypeEnum authenticationTypeEnum;

    @ApiModelProperty("优质服务")
    private List<String> firstRateService;

    @ApiModelProperty(value = "到店礼")
    private String toStoreGift;

    @ApiModelProperty(value = "商家类型 （1，酒店 2.主持人 3.默认）")
    private Integer storeType;

}
