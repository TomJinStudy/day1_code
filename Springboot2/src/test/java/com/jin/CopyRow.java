package com.jin;

import com.jin.domain.product;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpringBootTest
public class CopyRow {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        writeExcel1("E:\\ExcelTest\\报价单2.xlsx");
    }
    public static String[] fieldName(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName();
        }
        return fieldNames;
    }
    public static void writeExcel1(String finalXlsxPath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OutputStream out = null;
        File finalXlsxFile = createNewFile(finalXlsxPath);
        Workbook workBook = null;
        try {
            workBook = getWorkbok(finalXlsxFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Sheet sheet = workBook.getSheetAt(0);
        for (int i=0;i<10;i++){
           template1.copyRow(workBook,sheet.getRow(12),sheet.getRow(13+i),13+i);
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

    public  String setValues(product product,int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] fieldNames = fieldName(product.class);
        if (i>6) return "0";
        String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
        Method method = product.getClass().getMethod(methodName);
        // Object invoke = method.invoke(product);
          /* if(i<5){
               rowNew.createCell(i+1).setCellValue(invoke.toString());
           }
           rowNew.createCell(i+4).setCellValue(invoke.toString());*/

        return method.invoke(product).toString();
    }


}
