package cn.ruanyun.backInterface.modules.business.balance.VO;

import cn.ruanyun.backInterface.common.enums.AddOrSubtractTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class AppBalanceVO {


    /**
     * id
     */
    private String id;

    /**
     * 加减
     */
    private AddOrSubtractTypeEnum addOrSubtractTypeEnum;

    /**
     * 标题
     */
    private String title;


    /**
     * 金额
     */
    private String price;


    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
