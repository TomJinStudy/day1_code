package com.jin.dao.impl;

import com.jin.dao.userdao;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Component("userdao")
@Repository("userdao")
public class userdaoimpl implements userdao {
    @Override
    public void save() {
        System.out.println("save running");
    }
}
