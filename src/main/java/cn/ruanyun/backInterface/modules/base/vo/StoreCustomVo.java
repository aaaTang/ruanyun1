package cn.ruanyun.backInterface.modules.base.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreCustomVo {


    private String id;

    /**
     * 用戶昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 婚期
     */
    private String weddingDay;

    /**
     * 服务次数
     */
    private Integer serviceCount = 0;

}
