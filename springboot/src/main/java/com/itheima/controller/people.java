package com.itheima.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConfigurationProperties(prefix = "people")
public class people {
    private  String name;
    private  String age;

    @Override
    public String toString() {
        return "people{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", adress=" + Arrays.toString(adress) +
                '}';
    }

    private  String[] adress;

    public String[] getAdress() {
        return adress;
    }

    public void setAdress(String[] adress) {
        this.adress = adress;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

}
