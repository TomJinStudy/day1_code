package com.itheima.controller;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class peoplefiguration {
   @Bean
   public  people people(){
       return  new people();
   }
}
