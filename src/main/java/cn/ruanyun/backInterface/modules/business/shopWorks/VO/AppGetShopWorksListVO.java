package cn.ruanyun.backInterface.modules.business.shopWorks.VO;

import lombok.Data;

@Data
public class AppGetShopWorksListVO {

    private String id;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 作品图片
     */
    private String pic;

    /**
     * 视频
     */
    private String video;

    /**
     * 商家名称
     */
    private String shopName;
}
