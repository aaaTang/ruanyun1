package cn.ruanyun.backInterface.modules.business.size.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-14 13:59
 **/
@Data
@Accessors(chain = true)
public class SizeInfoVO {

    /**
     * id
     */
    private String id;

    /**
     * 名字
     */
    private String name;
}
