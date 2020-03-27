package cn.ruanyun.backInterface.modules.business.color.VO;

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
public class ColorInfoVO {

    private String id;

    private String title;
}
