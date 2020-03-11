package cn.ruanyun.backInterface.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fei
 */
@Data
public class IpLocate implements Serializable {

    private String retCode;

    private City result;
}

