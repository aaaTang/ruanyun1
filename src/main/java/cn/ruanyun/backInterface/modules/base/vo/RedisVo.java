package cn.ruanyun.backInterface.modules.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fei
 */
@Data
@AllArgsConstructor
public class RedisVo {

    private String key;

    private String value;
}
