package com.jin.dao.service.impl;

import com.jin.dao.service.userservice;
import com.jin.dao.userdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

//@Component("userservice")
@Service("userservice")
public class userservicrimpl implements userservice {
//    @Autowired
//    @Qualifier("userdao")
    @Resource(name = "userdao")//相当于Autowired+Qualfiler 名称注入
    private com.jin.dao.userdao userdao;

    @Value("sbbfsuobgvsukj")
    private  String nn;


    @Override
    public void save() {
        System.out.println(nn);
        userdao.save();
    }
    @PostConstruct
    public void init() {
        System.out.println(nn);

    }
    @PreDestroy
    public void story() {
        System.out.println(nn);
        userdao.save();
    }
}
