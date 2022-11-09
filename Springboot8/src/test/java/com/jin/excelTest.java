package com.jin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jin.Service.customerService;
import com.jin.Service.productService;
import com.jin.domain.product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class excelTest {
    @Autowired
    private customerService customerService;
    @Autowired
    private productService productService;
    @Test
    void tset1(){
        Workbook workbook=new HSSFWorkbook();
//2.根据workbook创建sheet
        int id=1;
       // String[] header = {"公司名称", "地址", "电话号","业务员","客户名称","公司联系传真","收汇方式"};
        String[] header1 = {"编码", "图片", "最小起订量","规格型号","产品描述","包装描述","价格"};
        List<product> data1 = new ArrayList<>();
        LambdaQueryWrapper<product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(product::getCustomerid,id);
        List<product> list1 = productService.list(wrapper);
        for (product temp:list1) {
            data1.add(temp);
        }
        export(data1, header1);
    }
    private static void export(List<product> data,  String[] header) {
        String[] fieldNames = fieldName(product.class);
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("测试列表");
        Row row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("[公司图片logo]");
        row1.createCell(3).setCellValue("[公司英文名称]");
        Row row = sheet.createRow(1);
        row.createCell(3).setCellValue("add:");
        row.createCell(5).setCellValue("【公司英文地址】");
        Row row2 = sheet.createRow(2);
        row2.createCell(3).setCellValue("payment:");
        row2.createCell(4).setCellValue("【收汇方式】");
        Row row3 = sheet.createRow(3);
        row3.createCell(3).setCellValue("tel");
        row3.createCell(4).setCellValue("[联系电话]");
        row3.createCell(5).setCellValue("fax");
        row3.createCell(6).setCellValue("公司联系传真");
        Row row4 = sheet.createRow(4);
        row4.createCell(3).setCellValue("contact");
        row4.createCell(4).setCellValue("业务员英文名称");
        row4.createCell(5).setCellValue("to");
        row4.createCell(6).setCellValue("客户名称");
        sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 6));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));
        int rowSize = 5;
       // Sheet sheet = wb.createSheet();
        Row row5 = sheet.createRow(rowSize);
        try {
            for (int i = 0; i < header.length; i++) {
                row5.createCell(i).setCellValue(header[i]);
            }
            for (int x = 0; x < data.size(); x++) {
                rowSize = 6;
                Row rowNew = sheet.createRow(rowSize + x);
                for (int i = 0; i < header.length; i++) {
                    product product = data.get(x);
                    for (int i1 = 0; i1 < fieldNames.length; i1++) {
                        String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
                        Method method = product.getClass().getMethod(methodName);
                        Object invoke = method.invoke(product);//获取属性值
                        rowNew.createCell(i).setCellValue(invoke.toString());
                    }
                }
            }
        } catch (Exception e) {
        }
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("E:/ExcelTest/product9.xls");
            wb.write(outputStream);
        } catch (Exception e) {
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {

            }
            try{
                if(wb != null){
                    wb.close();
                }
            } catch (Exception e){

            }
        }

    }

    private static String[] fieldName(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName(); //通过反射获取属性名
        }
        return fieldNames;
    }
}
