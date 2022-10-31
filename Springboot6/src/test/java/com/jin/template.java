package com.jin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class template {

    public static void main(String[] args) {

        writeExcel1("E:\\ExcelTest\\报价单.xlsx");

    }

    public static void writeExcel1(String finalXlsxPath) {
        List<Object> list=new ArrayList<>();
        OutputStream out = null;
        File finalXlsxFile = createNewFile(finalXlsxPath);
        Workbook workBook = null;
        try {
            workBook = getWorkbok(finalXlsxFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Sheet sheet = workBook.getSheetAt(0);
       /* for(int rowNum=1;rowNum<30;rowNum++){
            Row row = sheet.getRow(rowNum);
            for (int cellNum=0;cellNum<30;cellNum++){
                Cell cell = row.getCell(cellNum);
                if (cell==null){
                 continue;
                }
                list.add(cell.getAddress());

            }*/


        /*Row row6 = sheet.getRow(1);
        row6.getCell(1).setCellValue("6行5列的值");
        row6.getCell(3).setCellValue("6行6列的值");
        Row row7 = sheet.getRow(2);
        row7.getCell(4).setCellValue("6行7列的值");
        row7.getCell(15).setCellValue("6行8列的值");
        Row row8 = sheet.getRow(3);
        row8.getCell(4).setCellValue("6行7列的值");
        row8.getCell(8).setCellValue("6行8列的值");
        row8.getCell(15).setCellValue("6行8列的值");
        Row row9 = sheet.getRow(4);
        row9.getCell(4).setCellValue("6行9列的值");
        row9.getCell(8).setCellValue("6行10列的值");
        row9.getCell(15).setCellValue("6行8列的值");
        Row row10 = sheet.getRow(5);
        row10.getCell(4).setCellValue("7行5列的值");
        row10.getCell(8) .setCellValue("6行10列的值");*/
       /* Row row12 = sheet.getRow(7);
        row12.getCell(14) .setCellValue(UUID.randomUUID().toString());
        Row row13 = sheet.getRow(8);
        row13.getCell(14) .setCellValue(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));*/
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
   /* setCellValue(sheet,1,1,"公司的图片");
    setCellValue(sheet,1,3,data1.get(1).toString());
    setCellValue(sheet,2,4,data1.get(2).toString());
    setCellValue(sheet,2,15,data1.get(8).toString());
    setCellValue(sheet,3,4,data1.get(3).toString());
    setCellValue(sheet,3,8,data1.get(6).toString());
    setCellValue(sheet,3,15,data1.get(10).toString());
    setCellValue(sheet,4,4,data1.get(4).toString());
    setCellValue(sheet,4,8,data1.get(9).toString());
    setCellValue(sheet,4,15,data1.get(11).toString());
    setCellValue(sheet,5,4,data1.get(5).toString());
    setCellValue(sheet,5,8,data1.get(4).toString());*/
    @Test
    void test2(){

    }
}



