package cn.ruanyun.backInterface.modules.business.searchHistory.VO;

import lombok.Data;

@Data
public class AppSearchHistorVO {

    private String id;
    /**
     * 搜索的名称
     */
    private String title;
    /**
     * 搜索数量
     */
    private Integer count;
}
