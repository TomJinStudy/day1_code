package com.jin.dao.service.impl;

import com.jin.dao.service.userservice;
import com.jin.dao.usedao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class userservceimpl implements userservice {
    private  usedao usedao;

    public userservceimpl(usedao usedao) {
        this.usedao = usedao;
    }
    public  userservceimpl(){

    }
/* public void setUsedao(usedao usedao) {
        this.usedao = usedao;
    }*/

    @Override
    public void save() {
       /* ApplicationContext app=new ClassPathXmlApplicationContext("application.xml");
        usedao usedao=(usedao)app.getBean("usedao");
        usedao usedao1=(usedao)app.getBean("usedao");*/
        usedao.save();


    }
}
