package com.zhang.exceptionhandler;

import com.zhang.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Slf4j ：作日志输出用
 */
@ControllerAdvice //增强类 一般做全局异常处理
@Slf4j //如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(XXX.class); 可以用注解 @Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理..");
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody //为了返回数据
    public R error(GuliException e) {
        log.error(e.getMessage());//将错误信息输出到日志文件里面
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
