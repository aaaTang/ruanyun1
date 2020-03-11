package cn.ruanyun.backInterface.common.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @author ruanyun
 */
public class LoginFailLimitException extends InternalAuthenticationServiceException {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginFailLimitException(String msg){
        super(msg);
        this.msg = msg;
    }

    public LoginFailLimitException(String msg, Throwable t) {
        super(msg, t);
        this.msg = msg;
    }
}
