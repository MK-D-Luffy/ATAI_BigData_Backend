package com.atai.servicebase.exceptionhandler;


import com.atai.commonutils.result.R;
import com.atai.commonutils.util.ExceptionUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler (Exception.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message(e.getMessage());
    }

    //特定异常   先找特定异常  再找全局异常
    @ExceptionHandler (ArithmeticException.class)
    @ResponseBody //为了返回数据
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行了算术异常处理..");
    }

    //自定义异常
    @ExceptionHandler (MSException.class)
    @ResponseBody //为了返回数据
    public R error(MSException e) {
        log.error(e.getMessage());
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();

        return R.error().code(e.getCode()).message(e.getMessage());
    }
}
