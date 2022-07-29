package com.jin;

import com.jin.dao.usedao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {
    @Test//测试scope
    public  void Test(){
        ApplicationContext app=new ClassPathXmlApplicationContext("application.xml");
        usedao usedao=(usedao)app.getBean("usedao");
        usedao usedao1=(usedao)app.getBean("usedao");
        usedao.save();
        System.out.println(usedao);
        System.out.println(usedao1);

    }
}
