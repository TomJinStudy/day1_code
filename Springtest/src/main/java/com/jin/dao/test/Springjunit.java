package com.jin.dao.test;

import com.jin.dao.config.springconfiguration;
import com.jin.dao.service.userservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:application.xml")
@ContextConfiguration(classes ={springconfiguration.class} )
public class Springjunit {
    @Autowired
    private userservice  userservice;
    @Test
    public void test(){


     userservice.save();

    }
}
