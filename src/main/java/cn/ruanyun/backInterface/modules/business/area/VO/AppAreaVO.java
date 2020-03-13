package cn.ruanyun.backInterface.modules.business.area.VO;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * APP数据输出层
 */
@Data
@Accessors(chain = true)
public class AppAreaVO {



    private String id;

    /**
     * 城市名称
     */
    private String title;


}
