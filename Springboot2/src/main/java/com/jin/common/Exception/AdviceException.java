package com.jin.common.Exception;

import com.jin.common.Reslut;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceException {

    @ExceptionHandler
    public Reslut exception(Exception e){
           e.printStackTrace();
        return Reslut.error("出现异常请重试");
    }
    @ExceptionHandler(defintException.class)
    public Reslut defintException(defintException defintException){
        String message = defintException.getMessage();
        return Reslut.error(message);
    }
}
