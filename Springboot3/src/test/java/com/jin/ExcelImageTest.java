package com.jin;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFShape;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static cn.hutool.core.img.ImgUtil.toBufferedImage;

public class ExcelImageTest {
    public static void main(String[] args) {
        FileOutputStream fileOut = null;
        BufferedImage bufferImg = null;
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        try {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet1 = wb.createSheet("test picture");
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            //bufferImg = ImageIO.read(new File("E:/img/12.jpg"));
            Image image = Toolkit.getDefaultToolkit().getImage("E:/img/12.jpg");
            image.flush();
            bufferImg = toBufferedImage(image);
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
           // long col2 = Math.round((bufferImg.getWidth() / (96 / 2.54)) / (sheet1.getColumnWidthInPixels(0) / (96 / 2.54)));
            //int row2 = (int)Math.round((bufferImg.getHeight() / (96 / 2.54)) / (sheet1.getDefaultRowHeightInPoints() / 28.35));
            float i = bufferImg.getWidth() / bufferImg.getHeight();
           // float proportion=col2/row2;
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            XSSFDrawing shapes =(XSSFDrawing) sheet1.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
           ClientAnchor anchor = new XSSFClientAnchor(25, 125, 255, 255,(short)0 , 3, (short) 1,5);
           // ClientAnchor anchor = new XSSFClientAnchor();
            anchor.setDx1(9525 * 255);
            anchor.setDy1(9525*125);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            //插入图片
            shapes.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
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
    public static void insertPicture(Sheet sheet1, Cell cell, String img, int col1, int row1, int col2, int row2){
        BufferedImage bufferImg = null;
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File(img));
            Image image = Toolkit.getDefaultToolkit().getImage(img);
            image.flush();
            bufferImg = toBufferedImage(image);
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            Drawing<?> shapes = sheet1.createDrawingPatriarch();
            float i = bufferImg.getWidth() / bufferImg.getHeight();
            int columnWidth = sheet1.getColumnWidth(cell.getColumnIndex());
            short height = cell.getRow().getHeight();
            float w=i*height;
            ClientAnchor anchor = new XSSFClientAnchor(125, 125, 255, 255,(short) col1, row1, (short) col2, row2);
            shapes.createPicture(anchor,sheet1.getWorkbook().addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
    } catch (Exception e) {
        e.printStackTrace();
    }}
}
