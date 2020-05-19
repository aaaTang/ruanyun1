package cn.ruanyun.backInterface.modules.business.privateNumberAx.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-19 19:13
 **/
@Data
@Accessors(chain = true)
public class PrivateNumberAxVo {


    private String id;

    /**
     * 客户绑定名称
     */
    private String callerName;

    /**
     * 客户绑定手机号
     */
    private String callerPhone;

    /**
     * 商家绑定名称
     */
    private String calleeName;

    /**
     * 商家绑定手机号
     */
    private String calleePhone;

    /**
     * 虚拟号段
     */
    private String privateNumber;

    /**
     * 绑定ID，唯一标识一组绑定关系。成功响应时必定返回。请记录该ID用于后续接口调用。
     */
    private String subscriptionId;
}
