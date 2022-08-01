package com.jin;

import com.jin.aop.Target;
import com.jin.aop.interfaceTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class Aoptest {

    @Autowired
    private interfaceTarget target;

    @Test
    private void test(){
        target.save();
    }

}
