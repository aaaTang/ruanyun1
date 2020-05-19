package cn.ruanyun.backInterface.modules.business.privateNumberAx.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-19 16:39
 **/

@Data
@Accessors(chain = true)
public class ReturnJson {

    /**
     * 请求返回的结果码
     */
    private String resultcode;

    /**
     * 操作结果描述
     */
    private String resultdesc;

    /**
     * A号码。成功响应时必定返回
     */
    private String origNum;

    /**
     * 绑定的X号码。成功响应时必定返回
     */
    private String relationNum;

    /**
     * 绑定ID，唯一标识一组绑定关系。成功响应时必定返回。请记录该ID用于后续接口调用。
     */
    private String subscriptionId;

    /**
     * 绑定关系允许的呼叫方向，取值含义参见请求参数。
     *
     * 成功响应时必定返回。
     */
    private Integer callDirection;

    /**
     * 绑定关系保持时间，单位为秒，0表示永不过期。
     *
     * 成功响应时必定返回。
     */
    private Integer duration;

    /**
     *
     * 允许单次通话进行的最长时间，通话时间从被叫接通的时刻开始计算，0表示系统不主动结束通话。
     *
     * 成功响应时必定返回。
     */
    private Integer maxDuration;
}
