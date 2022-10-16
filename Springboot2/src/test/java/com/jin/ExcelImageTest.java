package com.jin;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelImageTest {
    public static void main(String[] args) {
        FileOutputStream fileOut = null;
        BufferedImage bufferImg = null;
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File("E:/img/12.jpg"));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            Workbook wb = new XSSFWorkbook();
            Sheet sheet1 = wb.createSheet("test picture");
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            Drawing<?> shapes = sheet1.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) 3, 3, (short) 6, 10);
            anchor.setAnchorType(ClientAnchor.AnchorType.byId(3) );
            //插入图片
            shapes.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
            fileOut = new FileOutputStream("E:/ExcelTest/测试Excel"+System.currentTimeMillis()+".xlsx");
            // 写入excel文件
            wb.write(fileOut);
            System.out.println("----Excel文件已生成------");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fileOut != null){
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void insertPicture(Sheet sheet1,String img,int col1,int row1,int col2,int row2){
        BufferedImage bufferImg = null;
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File(img));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            Drawing<?> shapes = sheet1.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) col1, row1, (short) col2, row2);
            shapes.createPicture(anchor,sheet1.getWorkbook().addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
    } catch (Exception e) {
        e.printStackTrace();
    }}
}
