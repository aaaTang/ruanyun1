package cn.ruanyun.backInterface.modules.business.storeAudit.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 数据审核输出层
 */
@Data
@Accessors(chain = true)
public class StoreAuditListVO {

    private String id;

    /**
     * 姓名或者店铺名称
     */
    private String username;


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

}
