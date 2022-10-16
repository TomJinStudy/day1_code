package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Mapper.customerMapper;
import com.jin.Service.customerService;
import com.jin.domain.customer;
import com.jin.domain.product;
import com.jin.domain.template2;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import static com.jin.Service.impl.customerServiceimpl1.getWorkbok;

@Service
public class customerServiceimpl1 extends ServiceImpl<customerMapper, customer> implements customerService {

   public  void writeExcel(String finalXlsxPath,List<product> data,List<String> data1) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       DataFormatter formatter=new DataFormatter();
       OutputStream out = null;
       File finalXlsxFile = createNewFile(finalXlsxPath);
       Workbook workBook = null;
       try {
           workBook = getWorkbok(finalXlsxFile);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
       int size=0;
       int rowIndex=0;
       String[] customerfield = fieldName1(customer.class);
       String[] productfiled = fieldName1(product.class);
       Sheet sheet = workBook.getSheetAt(0);
       int startRow = getRownum(sheet, rowIndex);
       for (int i=0;i< data.size()-1;i++){
           template2.copyRow(workBook,sheet.getRow(startRow),sheet.getRow(startRow+i+1),startRow+i+1);
       }
       for (int rownum=1;rownum<30;rownum++){
           Row row = sheet.getRow(rownum);
           if(row==null){
              continue;
           }
           for (int cellNum=0;cellNum<30;cellNum++){
              Cell cell= row.getCell(cellNum);
              if (cell==null)  continue;
              String s = formatter.formatCellValue(cell);
              if(s!=null&&s.contains("[")){
                  String substring = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                 for (int i=0;i<customerfield.length;i++){
                     boolean equals = customerfield[i].equals(substring);
                     if (equals){
                         if (substring.equals("img")) {
                             int colIndex = cell.getColumnIndex();
                             int rowNum = cell.getRow().getRowNum();
                             insertPicture(sheet,data1.get(i), colIndex , rowNum , colIndex+2, rowNum+5);
                         }else {
                             cell.setCellValue(data1.get(i));
                         }
                     }
                 }
                }
           }
       }
    //   double index = 1;
       for (int rownum=startRow;rownum<30;rownum++,size++){
           Row row = sheet.getRow(rownum);
           if(row==null){
               continue;
           }
         //  if (index<=data.size())  row.createCell(0).setCellValue(index);
           for (int num=0;num<30;num++){
           Cell cell= row.getCell(num);
           if (cell==null)  continue;
           String s = formatter.formatCellValue(cell);
           if(s!=null&&s.contains("<")) {
               String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
               for (int i=0;i<productfiled.length;i++){
                   boolean equals = productfiled[i].equals(substrings);
                   if (equals){
                       if (substrings.equals("picture")) {
                           int colIndex = cell.getColumnIndex();
                           int rowNum = cell.getRow().getRowNum();
                           insertPicture(sheet, setValues(data.get(size),i), colIndex , rowNum , colIndex+1, rowNum+1);
                       }else {
                           cell.setCellValue(setValues(data.get(size),i));
                       }

                   }

               }
           }
           }
       }
       sheet.setForceFormulaRecalculation(true);
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
    public  String[] fieldName1(Class clazz) {
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
   public  String setValues(product product,int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       String[] fieldNames = fieldName1(product.class);
           String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
           Method method = product.getClass().getMethod(methodName);
       return method.invoke(product).toString();
   }
    public  static int getRownum(Sheet sheet,int Rownum ){
        DataFormatter formatter = new DataFormatter();
        for (int rownum=1;rownum<30;rownum++){
            Row row = sheet.getRow(rownum);
            if(row==null) continue;
            for (int cellNum=0;cellNum<30;cellNum++){
                Cell cell= row.getCell(cellNum);
                if (cell==null)  continue;
                String s = formatter.formatCellValue(cell);
                if (s!=null&&s.contains("<")) {
                    if (Rownum==0) Rownum = cell.getRowIndex();
                }
            }
        }
        return  Rownum;
    }
    public static void insertPicture(Sheet sheet1,String img,int col1,int row1,int col2,int row2){
        BufferedImage bufferImg = null;
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File(img));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            Drawing<?> shapes = sheet1.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,(short) col1, row1, (short) col2, row2);
            shapes.createPicture(anchor,sheet1.getWorkbook().addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
