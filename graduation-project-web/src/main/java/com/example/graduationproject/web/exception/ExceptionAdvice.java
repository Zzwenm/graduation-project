package com.example.graduationproject.web.exception;

import com.example.graduationproject.web.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.SocketTimeoutException;

/**
 * @author Zzwen
 * @date 2020/4/25 10:02
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public ResultDto globalException(ArithmeticException e){
        log.error("全局异常拦截：" + e);
        return new ResultDto<>(50000,"something error!",null);
    }

    @ResponseBody
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResultDto globalException(RedisConnectionFailureException e){
        log.error("全局异常拦截：" + e);
        return new ResultDto<>(50012,"redis连接超时!",null);
    }
}
