package com.jin.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.text.normalizer.NormalizerBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
@ResponseBody
public class usercontroller {


    @RequestMapping("/qucik1")
    public ModelAndView save(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ggiu","yygygg");
        modelAndView.setView("index");
        return modelAndView;
    }
    @RequestMapping("/qucik2")
    public ModelAndView save2(ModelAndView modelAndView){
        modelAndView.addObject("ggiu","yygygg");
        modelAndView.setViewName("index.jsp");
        return modelAndView;
    }
    @RequestMapping("/qucik3")
    public String save3(Model mode){
        mode.addAttribute("username","jinyongchao");
        return "iuiguig";
    }
    @RequestMapping("/qucik4")
    public String save4(HttpServletRequest request){
    request.setAttribute("ioh","yg");
        return "ygyuf";
    }
    @RequestMapping("/qucik5")
    public void save5(HttpServletResponse response) throws IOException {
        response.getWriter().write("khhk");
    }
    @RequestMapping("/qucik6")
    @ResponseBody
    public String save6(){
        ObjectMapper objectMapper = new ObjectMapper();
       String json= objectMapper.wrietValueAsString(user);
        return" modelAndView";
    }
    @RequestMapping("/qucik6")
    @ResponseBody
    public user save6(){
        user user = new user();
        user.set(username);

        return user;
    }
    @RequestMapping("/qucik7")
    @ResponseBody
    public void save7(String username ,int age){
        System.out.println(username);
        System.out.println(age);

    }
    @RequestMapping("/qucik8")
    @ResponseBody
    public void save8(user user){

        System.out.println(user);

    }
    @RequestMapping("/qucik7")
    @ResponseBody
    public void save7(String username ,int age){
        System.out.println(username);
        System.out.println(age);

    }
}
