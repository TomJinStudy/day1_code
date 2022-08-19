package com.jin.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class logininterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long employee = (Long) session.getAttribute("employee");
        Long user = (Long) session.getAttribute("user");
        if (employee!=null || user!=null){
            return  true;
        }
        response.sendRedirect("/backend/page/login/login.html");
        return false;
    }
}
