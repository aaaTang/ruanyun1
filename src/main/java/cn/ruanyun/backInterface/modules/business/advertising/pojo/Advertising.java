package cn.ruanyun.backInterface.modules.business.advertising.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AdvertisingJumpTypeEnum;
import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 广告管理
 * @author fei
 */
@Data
@Entity
@Table(name = "t_advertising")
@TableName("t_advertising")
public class Advertising extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 广告类型
     */
//    private AdvertisingTypeEnum advertisingType;
      private String advertisingType;


    /**
     * 跳转类型
     */
//    private AdvertisingJumpTypeEnum advertisingJumpType;
      private String advertisingJumpType;


    /**
     * 图片
     */
    private String pic;


    /**
     * 链接
     */
    private String url;

}