package com.jin.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.customer;
import com.jin.domain.product;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface customerService extends IService<customer> {
    void writeExcel(String finalXlsxPath,List<product> data,List<String> data1,List<product> group1,List<product> group2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    String[] fieldName1(Class clazz);
}
