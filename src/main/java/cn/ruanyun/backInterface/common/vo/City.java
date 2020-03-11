package cn.ruanyun.backInterface.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fei
 */
@Data
public class City implements Serializable {

    String country;

    String province;

    String city;
}
