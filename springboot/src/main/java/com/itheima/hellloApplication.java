package com.itheima;

import com.itheima.controller.people;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/*
* 应到类 springboot入口
* */

@SpringBootApplication
@Import(people.class)
public class hellloApplication {
    public static void main(String[] args) {
        SpringApplication.run(hellloApplication.class,args);
     people people=context.getBean(people.class);
        System.out.println(people);
       context

    }
}
