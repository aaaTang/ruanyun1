package cn.ruanyun.backInterface.common.exception;


import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fei
 */
@Slf4j
@RestControllerAdvice
public class RestCtrlExceptionHandler {

    @ExceptionHandler(RuanyunException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleRuyunException(RuanyunException e) {

        String errorMsg = "Ruanyun exception";
        if (e!=null){
            errorMsg = e.getMsg();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleException(Exception e) {

        String errorMsg = "Exception";
        if (e!=null){
            errorMsg = e.getMessage();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }
}
