package com.jin.dao.impl;

import com.jin.dao.service.impl.user;
import com.jin.dao.usedao;

import java.util.List;
import java.util.Map;

public class userdaoimpl implements usedao {
    private  String username;
    private  int age;
    private List<String> list;
    private Map<String, user> hashmap;

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setHashmap(Map<String, user> hashmap) {
        this.hashmap = hashmap;
    }

    public userdaoimpl() {
        System.out.println("userimpl running");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void save() {
        System.out.println("username===="+username+"age==="+age);
        System.out.println("save running");
        System.out.println(list);
        System.out.println(list);
        System.out.println(hashmap);
    }
    /*public  void  destory(){
        System.out.println("销毁方法");
    }*/
}
