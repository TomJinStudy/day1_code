package com.jin.aop;

import org.springframework.stereotype.Component;

@Component
public class Target implements interfaceTarget{


    @Override
    public void save() {
        System.out.println("正在运行");
    }
}
