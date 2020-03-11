package cn.ruanyun.backInterface.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RuanyunException extends RuntimeException {

    private String msg;

    public RuanyunException(String msg){
        super(msg);
        this.msg = msg;
    }
}
