package cn.ruanyun.backInterface.modules.base.dto;

import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreListDto {

    @ApiModelProperty(value = "分类id")
    private String goodCategoryId;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    /*@ApiModelProperty(value = "数据总大小")
    private Long totalSize;

    @ApiModelProperty(value = "当前页")
    private Long currentPageNum;*/

    @ApiModelProperty("门店等级 判断门店等级 1.没有等级 2.普通 3.铜牌 4.银牌 5.金牌 6.钻石")
    private Integer storeLevel;
    /**
     * 销量升序 1，销量降序2
     * 价格升序3，价格价格4
     * 评论数升 序5，评论数降序6
     * 门店等级升序 7 门店等级降序8
     * 门店星级升序 9 门店星级降序10
     * 距离升序 11 距离降序12
     */
    private Integer filterCondition;
}
