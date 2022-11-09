package com.jin.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jin.Mapper.productMapper;
import com.jin.Service.customerService;
import com.jin.Service.impl.customerServiceimpl1;
import com.jin.Service.productService;
import com.jin.common.Exception.defintException;
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
    @Autowired
    private productMapper productMapper;

    public static int MulNums=1;
    public static boolean MulDetail = false;
    @GetMapping("/findmsg/{id}")
    public Reslut Template(@PathVariable Integer id,@RequestParam(defaultValue = "1") Integer n) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String[] fieldName = customerService.fieldName1(customer.class);
        List<String> mainTable=new ArrayList<>();
        customer customer = customerService.getById(id);
        for (int i=0;i<fieldName.length;i++){
            String fieldname = "get"+fieldName[i].substring(0, 1).toUpperCase() + fieldName[i].substring(1);
            Method method = customer.getClass().getMethod(fieldname);
            String invoke = method.invoke(customer).toString();
            mainTable.add(invoke);}
        LambdaQueryWrapper<product> wrapper = new LambdaQueryWrapper<>();
        wrapper.groupBy(product::get外销报价单号,product::get中文品名,product::getId).eq(product::getCustomerid,id);
        List<product> SubTable = productService.list(wrapper);
        QueryWrapper<product> wrapper1 = new QueryWrapper<>();
        wrapper1.select("中文品名,sum(inner1) 销售数量,sum(price) price,sum(inner1*price) 成交金额 ").groupBy("外销报价单号","中文品名").eq("customerid",id);
        List<product> group1 = productService.list(wrapper1);
        QueryWrapper<product> wrapper2 = new QueryWrapper<>();
        wrapper2.select("外销报价单号,sum(inner1) 销售数量,sum(price) price,sum(inner1*price) 成交金额 ").groupBy("外销报价单号").eq("customerid",id);
        List<product> group2 = productService.list(wrapper2);
        List<product> sortIndex = productMapper.resort(id);
        for (int i=0;i<group2.size();i++){
            group2.get(i).setCount1(sortIndex.get(i).getCount1());}
        for (product temp:group2){
            System.out.println(temp);}
        String filePath;
        String group1Column="中文品名";
        String group2Column="";
        switch (n){
            case 0:
                filePath="E:\\ExcelTest\\正文.xlsx";
                break;
            case 1:
                filePath="E:\\ExcelTest\\正文+明细.xlsx";
                break;
            case 2:
                filePath="E:\\ExcelTest\\正文+一级分组+明细.xlsx";
                break;
            case 3:
                filePath="E:\\ExcelTest\\正文+二级分组+一级分组+明细.xlsx";
                break;
            case 4:
                filePath="E:\\ExcelTest\\正文+一级分组.xlsx";
                break;
            case 5:
                filePath="E:\\ExcelTest\\正文+二级分组.xlsx";
                break;
            case 6:
                filePath="E:\\ExcelTest\\正文+二级分组+一级分组.xlsx";
                break;
            case 7:
                filePath="E:\\ExcelTest\\多明细.xlsx";
                break;
            default:
                throw new defintException(" excel文件路径不存在，请检查！");

        }
        if (n==7) {
            MulDetail=true;
            for (int i=0;i<2;i++) {
                customerService.writeExcel(filePath, SubTable, mainTable, group1, group2, group1Column, group2Column, MulNums, MulDetail);
                MulNums++;
            }
            }else {
            customerService.writeExcel(filePath, SubTable, mainTable, group1, group2, group1Column, group2Column, MulNums, MulDetail);
        }
        return  Reslut.success(mainTable);

    }
}
