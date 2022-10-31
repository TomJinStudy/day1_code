package com.jin.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        List<String> mainTable=new ArrayList<>();
        customer customer = customerService.getById(id);
        for (int i=0;i<fieldName.length;i++){
            String fieldname = "get"+fieldName[i].substring(0, 1).toUpperCase() + fieldName[i].substring(1);
            Method method = customer.getClass().getMethod(fieldname);
            String invoke = method.invoke(customer).toString();
            mainTable.add(invoke);}
        LambdaQueryWrapper<product> wrapper = new LambdaQueryWrapper<>();
        wrapper.groupBy(product::getMoq,product::getId).eq(product::getCustomerid,id);
        List<product> SubTable = productService.list(wrapper);
        QueryWrapper<product> wrapper1 = new QueryWrapper<>();
        //wrapper1.select("sum(price)").groupBy(product::getMoq).eq(product::getCustomerid,id);
        wrapper1.select("moq,sum(inner1) inner1,sum(price) price,sum(inner1*price) outsource ").groupBy("moq").eq("customerid",id);
        List<product> group1 = productService.list(wrapper1);
        for (product temp :group1) {
            System.out.println(temp);
        }
        customerService.writeExcel("E:\\ExcelTest\\正文+一级分组+明细.xlsx",SubTable,mainTable,group1,null);
        return  Reslut.success(mainTable);
        /*   List<product> data1=new ArrayList<>();
        for (int i=0;i<list1.size();i++) {
            int index=-1;
            for (int j=i+1;j<list1.size();j++){
                if (list1.get(i).getMoq().equals(list1.get(j).getMoq())){
                    data1.add(list1.get(j));
                    list1.remove(j);
                    j--;
                    index=i;}}
            if (index!=-1){
                data1.add(list1.get(index));
                list1.remove(index);
                i--;}}
        for (int i=0;i<list1.size();i++){
            data1.add(list1.get(i));}*/
       /* int j=0;
        for (int i=0;i<data1.size();i++){
         j+=data1.get(i).getInner1();
        }
        System.out.println(j);*/
    }
}
