package com.jin.config;

import com.jin.Controller.util.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class suppert extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/front/**").addResourceLocations("classpath:front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:backend/");

    }



    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logininterceptor())
                .addPathPatterns("/backend/**","/front/**")
                .excludePathPatterns("/employee/login"
                        ,"/employee/logout"
                        ,"/backend/page/login/login.html"
                        ,"/front/page/login.html"
                        ,"/user/login"
                        ,"/user/loginout");
    }
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mapping=new MappingJackson2HttpMessageConverter();
        mapping.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,mapping);
    }
    @Bean
    public logininterceptor logininterceptor(){
        logininterceptor logininterceptor=new logininterceptor();
        return  logininterceptor;
    }
}
