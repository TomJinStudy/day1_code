package com.jin.Controller.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class MyMeteObject implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
       metaObject.setValue("createTime", LocalDateTime.now());
       metaObject.setValue("updateTime", LocalDateTime.now());
       metaObject.setValue("createUser",new Long(1));
       metaObject.setValue("updateUser", new Long(1));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(1));
    }
}
