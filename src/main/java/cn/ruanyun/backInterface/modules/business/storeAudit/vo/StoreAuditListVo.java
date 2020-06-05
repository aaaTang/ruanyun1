package cn.ruanyun.backInterface.modules.business.storeAudit.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据审核输出层
 */
@Data
@Accessors(chain = true)
public class StoreAuditListVo {

    private String id;

    /**
     * 姓名或者店铺名称
     */
    private String shopName;

    /**
     * 店铺图片
     */
    private String avatar;

    /**
     * 套餐数量
     */
    private int goodsPackageCuount;


    /**·
     * 关注数量
     */
    private int followCount;


    /**
     * 评价数量
     */
    private int commonCount;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机
     */
    private String mobile;

}
