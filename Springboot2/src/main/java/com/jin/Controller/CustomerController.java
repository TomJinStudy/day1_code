package com.jin.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jin.Service.customerService;
import com.jin.Service.productService;
import com.jin.common.Reslut;
import com.jin.domain.customer;
import com.jin.domain.product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private customerService customerService;
    @Autowired
    private productService productService;
    @GetMapping("/findmsg/{id}")
    public Reslut Template(@PathVariable Integer id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String[] fieldName = customerService.fieldName1(customer.class);
        List<String> data=new ArrayList<>();
        Map<String,String> map= new HashMap<>();
        customer customer = customerService.getById(id);
        for (int i=0;i<fieldName.length;i++){
            String fieldname = "get"+fieldName[i].substring(0, 1).toUpperCase() + fieldName[i].substring(1);
            Method method = customer.getClass().getMethod(fieldname);
            String invoke = method.invoke(customer).toString();
            data.add(invoke);}
        LambdaQueryWrapper<product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(product::getCustomerid,id);
        List<product> list1 = productService.list(wrapper);
        customerService.writeExcel("E:\\ExcelTest\\报价单4.xlsx",list1,data);
        return  Reslut.success(data);
    }


}
