package com.jin.controller.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class projectexceptionadvice {
    @ExceptionHandler
    public R ExceptionAdvice(Exception e){
        e.printStackTrace();
       return new R(false,"出现异常请重试");
    }

}
