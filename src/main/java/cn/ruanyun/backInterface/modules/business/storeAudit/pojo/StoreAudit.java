package cn.ruanyun.backInterface.modules.business.storeAudit.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商家入驻审核
 *
 * @author fei
 */
@Data
@Entity
@Table(name = "t_store_audit")
@TableName("t_store_audit")
public class StoreAudit extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 商家类型
     */
    private StoreTypeEnum storeType;


    /**
     * 姓名或者店铺名称
     */
    private String username;


    /**
     * 手机
     */
    private String mobile;


    /**
     * 服务类型
     */
    private String classificationId;


    /**
     * 所在城市id
     */
    private String areaId;


    /**
     * 身份证正面
     */
    private String idCardFront;


    /**
     * 身份证反面
     */
    private String idCardBack;


    /**
     * 营业执照
     */
    private String businessCard;


    /**
     * 经度
     */
    private String longitude;


    /**
     * 纬度
     */
    private String latitude;


    /**
     * 审核意见
     */
    private String checkAdvice;


    /**
     * 审核状态
     */
    private CheckEnum checkEnum = CheckEnum.PRE_CHECK;

}
