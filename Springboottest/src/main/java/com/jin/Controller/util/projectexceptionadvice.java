package com.jin.Controller.util;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class projectexceptionadvice {
    @ExceptionHandler
    public R<String> ExceptionAdvice(Exception e){
        e.printStackTrace();
       return R.error("出现异常请重试");
    }
    @ExceptionHandler(myexception.class)
    public R<String> ExceptionAdvice1(myexception e){
        String message = e.getMessage();
        return R.error(message);
    }

}
