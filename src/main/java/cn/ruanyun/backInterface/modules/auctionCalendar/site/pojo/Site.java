package cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 场地
 * @author fei
 */
@Data
@Entity
@Table(name = "t_site")
@TableName("t_site")
public class Site extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("场地名称")
    private String siteName;

    @ApiModelProperty("场地规格参数")
    private String siteItemValue;





}