package com.itheima.test;
/*
* 测试类
* */

import com.itheima.controller.UserService;
import com.itheima.hellloApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= hellloApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public  void  Testadd(){
        userService.add();
    }
}
