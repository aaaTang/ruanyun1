package cn.ruanyun.backInterface.modules.business.advertising.VO;

import cn.ruanyun.backInterface.common.enums.AdvertisingJumpTypeEnum;
import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true)
public class AppAdvertisingListVO {

    private String id;
    /**
     * 广告类型
     */
    private String advertisingType;


    /**
     * 跳转类型
     */
    private String advertisingJumpType;


    /**
     * 图片
     */
    private String pic;


    /**
     * 链接
     */
    private String url;

}
