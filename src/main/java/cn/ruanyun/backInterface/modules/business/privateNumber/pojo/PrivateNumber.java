package cn.ruanyun.backInterface.modules.business.privateNumber.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 虚拟号
 * @author z
 */
@Data
@Entity
@Table(name = "t_private_number")
@TableName("t_private_number")
public class PrivateNumber extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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
    private BooleanTypeEnum bound = BooleanTypeEnum.NO;
}