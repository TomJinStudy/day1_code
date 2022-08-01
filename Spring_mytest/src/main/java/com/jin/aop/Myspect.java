package com.jin.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Myspect {
    @Before("execution(* com.jin.aop.Target.save(..))")
    public void sing(){

        System.out.println("前置增强");
    }
   /* public void after(){
        System.out.println("后增强");
    }*/
}
