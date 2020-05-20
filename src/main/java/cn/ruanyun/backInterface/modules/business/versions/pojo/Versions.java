package cn.ruanyun.backInterface.modules.business.versions.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备版本
 * @author z
 */
@Data
@Entity
@Table(name = "t_versions")
@TableName("t_versions")
public class Versions extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     *最新版本号
     */
    private String newVersion;
    /**
     * 最小支持版本号
     */
    private String minVersion;
    /**
     * apk下载url
     */
    private String apkUrl;
    /**
     * 更新文案
     */
    private String updateDescription;
    /**
     * 是否有更新
     */
    private Boolean isUpdate;
    /**
     * 是否强制更新
     */
    private Boolean forceUpdate;
}