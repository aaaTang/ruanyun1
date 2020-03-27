package cn.ruanyun.backInterface.modules.business.selectStore.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-03-27 22:53
 **/

@Data
@Accessors(chain = true)
public class SelectStoreListVO {

    private String id;

    /**
     * 用户名
     */
    private String username;


    /**
     * 头像
     */
    private String avatar;


    /**
     * 最低价格
     */
    private BigDecimal lowPrice;

}
