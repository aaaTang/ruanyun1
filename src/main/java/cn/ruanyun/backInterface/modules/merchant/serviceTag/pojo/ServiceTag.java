package cn.ruanyun.backInterface.modules.merchant.serviceTag.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 优质服务标签
 * @author z
 */
@Data
@Entity
@Table(name = "t_service_tag")
@TableName("t_service_tag")
public class ServiceTag extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    private  String classId;

    /**
     * 分类标签名称
     */
    private  String title;

}