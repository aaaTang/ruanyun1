package cn.ruanyun.backInterface.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author fei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ruanyun.ratelimit")
public class RuanyunLimitProperties {

    /**
     * 是否开启全局限流
     */
    private Boolean enable = false;

    /**
     * 限制请求个数
     */
    private Integer limit = 100;

    /**
     * 每单位时间内（毫秒）
     */
    private Integer timeout = 1000;
}
