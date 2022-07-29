package com.jin.dao.web;

import com.jin.dao.config.springconfiguration;
import com.jin.dao.service.userservice;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class userController {
    public static void main(String[] args) {
        ApplicationContext app= new AnnotationConfigApplicationContext(springconfiguration.class);
        userservice bean = (userservice)app.getBean("userservice");
        bean.save();

    }
}
