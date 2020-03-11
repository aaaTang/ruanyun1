package cn.ruanyun.backInterface.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fei
 */
@Data
public class EmailValidate implements Serializable {

    private String username;

    private String email;

    private String validateUrl;

    private String code;

    private String fullUrl;

    private String operation;
}
