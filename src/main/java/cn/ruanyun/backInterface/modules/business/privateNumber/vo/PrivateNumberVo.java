package cn.ruanyun.backInterface.modules.business.privateNumber.vo;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-19 19:11
 **/
@Data
@Accessors(chain = true)
public class PrivateNumberVo {


    private String id;

    /**
     * 虚拟号段
     */
    private String privateNum;

    /**
     * 城市码
     */
    private String cityCode;

    /**
     * 是否被绑定
     */
    private BooleanTypeEnum bound;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
