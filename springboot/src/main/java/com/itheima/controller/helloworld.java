package com.itheima.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;

@RestController
public class helloworld {
    @Value("${name}")
    private String name;
    @Value("${people.name}")
    private String name1;
    @Value("${people.age}")
    private String age;
    @Value("${adress[0]}")
    private String adress;
    @Value("${adress[1]}")
    private String adress1;
    @Autowired
    private Environment env;
    @Autowired
    private  people people;
    @RequestMapping("/hello")
    public  String hello(){
        System.out.println(name);
        System.out.println(name1);
        System.out.println(age);
        System.out.println(adress);
        System.out.println(adress1);
        System.out.println("-------------------------");
        System.out.println(env.getProperty("people.name"));
        System.out.println(env.getProperty("people.age"));
        System.out.println(env.getProperty("adress[0]"));
        System.out.println(people);
        String[] adress=people.getAdress();
        for (String s : adress) {
            System.out.println(s);
        }
        return " springboot-helloworld";
    }
}
