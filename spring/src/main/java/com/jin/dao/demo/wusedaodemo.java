package com.jin.dao.demo;

import com.jin.dao.service.impl.userservceimpl;
import com.jin.dao.service.userservice;
import com.jin.dao.usedao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class wusedaodemo {
    public static void main(String[] args) {
//        userservceimpl userservceimpl = new userservceimpl();
//            userservceimpl.save();
        ApplicationContext app = new ClassPathXmlApplicationContext("application.xml");
        userservice userservice = (userservice) app.getBean("userservice");
        userservice.save();

    }
}
