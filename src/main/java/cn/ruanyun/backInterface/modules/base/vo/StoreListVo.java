package cn.ruanyun.backInterface.modules.base.vo;

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

    @ApiModelProperty("门店等级 判断门店等级 0.没有等级 1.铜牌 2.银牌 3.金牌 4.钻石")
    private Integer storeLevel;

    @ApiModelProperty("门店名称")
    private String nickName;

    @ApiModelProperty(value = "门店星级")
    private Integer storeStarLevel;

    @ApiModelProperty("评价条数")
    private Integer commentNum;

    @ApiModelProperty("最低价格")
    private BigDecimal lowPrice;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("距离")
    private Double distance;

    // TODO: 2020/5/30 0030 连锁认证

    @ApiModelProperty("优质服务")
    private List<String> firstRateService;

    @ApiModelProperty(value = "到店礼")
    private String toStoreGift;
}
