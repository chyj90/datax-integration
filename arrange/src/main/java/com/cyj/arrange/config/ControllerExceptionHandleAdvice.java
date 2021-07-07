package com.cyj.arrange.config;

import com.cyj.arrange.bean.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller
 * 异常统一处理
 */
@RestControllerAdvice
public class ControllerExceptionHandleAdvice {
    @ExceptionHandler
    public Result handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Result result = new Result();
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setMessage(e.getMessage());
        return result;
    }
}
