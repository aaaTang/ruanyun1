package cn.ruanyun.backInterface.modules.business.size.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-10 19:48
 **/
@Data
@Accessors(chain = true)
public class SizeVO {

    private String id;

    /**
     * 尺寸名称
     */
    private String name;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
