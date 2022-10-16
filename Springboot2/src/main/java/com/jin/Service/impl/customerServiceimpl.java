package com.jin.Service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Mapper.customerMapper;
import com.jin.Service.customerService;
import com.jin.domain.customer;
import com.jin.domain.product;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/*
@Service
public class customerServiceimpl extends ServiceImpl<customerMapper, customer> implements customerService {

   public static String[] fieldName(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName();
        }
        return fieldNames;
    }
   public  void writeExcel(String finalXlsxPath,List<product> data,List<Object> data1) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       OutputStream out = null;
       File finalXlsxFile = createNewFile(finalXlsxPath);
       Workbook workBook = null;

       try {
           workBook = getWorkbok(finalXlsxFile);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
       Sheet sheet = workBook.getSheetAt(0);

       for (int i=0;i<data.size();i++){
           mergeRegin(sheet,13+i,13+i,5,8);
       }
       Row row6 = sheet.getRow(1);
       row6.getCell(1).setCellValue("公司的图片");
       row6.getCell(3).setCellValue(data1.get(1).toString());
       Row row7 = sheet.getRow(2);
       row7.getCell(4).setCellValue(data1.get(2).toString());
       row7.getCell(15).setCellValue(data1.get(8).toString());
       Row row8 = sheet.getRow(3);
       row8.getCell(4).setCellValue(data1.get(3).toString());
       row8.getCell(8).setCellValue(data1.get(6).toString());
       row8.getCell(15).setCellValue(data1.get(10).toString());
       Row row9 = sheet.getRow(4);
       row9.getCell(4).setCellValue(data1.get(4).toString());
       row9.getCell(8).setCellValue(data1.get(9).toString());
       row9.getCell(15).setCellValue(data1.get(11).toString());
       Row row10 = sheet.getRow(5);
       row10.getCell(4).setCellValue(data1.get(5).toString());
       row10.getCell(8) .setCellValue(data1.get(4).toString());
       Row row12 = sheet.getRow(7);
       row12.getCell(14) .setCellValue(UUID.randomUUID().toString().substring(0,16));
       Row row13 = sheet.getRow(8);
       row13.getCell(14) .setCellValue(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
       String[] fieldNames = fieldName(product.class);
       for (int x = 0; x < data.size(); x++) {
           int rowSize = 13;
           Row rowNew = sheet.createRow(rowSize + x);
           for (int i = 0; i < 7; i++) {
               product product = data.get(x);
               for (int i1 = 0; i1 < fieldNames.length; i1++) {
                   String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
                   Method method = product.getClass().getMethod(methodName);
                   Object invoke = method.invoke(product);
                   if(i<5){
                       rowNew.createCell(i+1).setCellValue(invoke.toString());
                   }
                   rowNew.createCell(i+4).setCellValue(invoke.toString());
               }
           }
       }
       try {
           out = new FileOutputStream(finalXlsxFile);
       } catch (FileNotFoundException e1) {
           e1.printStackTrace();
       }
       try {
           workBook.write(out);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
       try {
           if (out != null) {
               out.flush();
               out.close();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

    @Override
    public String[] fieldName1(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName();
        }
        return fieldNames;
    }

    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        wb = new XSSFWorkbook(in);
        return wb;
    }
    private static File createNewFile(String path) {
        File file = new File(path);
        String realPath = file.getParent();
        String newFileName = "报表-" + System.currentTimeMillis()
                + ".xlsx";
        File newFile = new File(realPath, newFileName);
        try {
            newFile.createNewFile();
            FileUtils.copyFile(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }
    public void mergeRegin(Sheet sheet,int up,int down,int l,int r){
       sheet.addMergedRegion(new CellRangeAddress(up,down,l,r));
    }
    public void setCellValue(Sheet sheet ,int i,int j,String n){
        Row row = sheet.getRow(i);
        row.createCell(j).setCellValue(n);

    }
*/
//}
