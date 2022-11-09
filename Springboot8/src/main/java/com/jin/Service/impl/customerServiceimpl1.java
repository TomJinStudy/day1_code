package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Mapper.customerMapper;
import com.jin.Service.customerService;
import com.jin.common.Exception.defintException;
import com.jin.domain.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.jin.Service.impl.customerServiceimpl1.getWorkbok;

@Service
public class customerServiceimpl1 extends ServiceImpl<customerMapper, customer> implements customerService {
   public static final int EMU_PER_PIXEL = 9525;
    //正本数据单元格的数组
   public static List<cellOriginal> CellOriginals;
    //明细数据单元格的数组
   public static List<cellOriginal> CellDetail;
    //浮动数据单元格的数组
   public static List<cellOriginal> CellSuspend;
    //一级分组数据单元格的数组
   //public static List<cellOriginal> CellGroup1;
   public static  List<cellOriginal> CellGroup;
    //二级分组数据单元格的数组
   //public static List<cellOriginal> CellGroup2;
   public static  List<cellOriginal> Cell2Group;

    public void writeExcel(String finalXlsxPath,List<product> data,List<String> data1,List<product> group1,List<product> group2,
                           String group1Column,String group2Column,int MulNums,boolean MulDetail) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       DataFormatter formatter=new DataFormatter();
       List<String> zhuBiao = new ArrayList<>();
       List<String> ziBiao = new ArrayList<>();
       OutputStream out = null;
       File finalXlsxFile = createNewFile(finalXlsxPath);
       Workbook workBook = null;
       try {
           workBook = getWorkbok(finalXlsxFile);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
       int size=0;
       int rowIndex=1;
       int cellGroupRow=0;
       int cellGroupcol=0;
       String[] customerfield = fieldName1(customer.class);
       String[] productfiled = fieldName1(product.class);
       for (int i=0;i<customerfield.length;i++){
           zhuBiao.add(customerfield[i]);}
       for (int i=0;i<productfiled.length;i++){
            ziBiao.add(productfiled[i]);}
       Sheet sheet = workBook.getSheetAt(0);
       initCell(sheet,MulNums,MulDetail);
       //判断字段是否存在
        //正文字段判断
        if (CellOriginals.size() > 0) {
            if (data1 == null || data1.size() == 0) {
                throw new defintException("打印的明细数据不存在");
                 }
            for (int iIndex = 0; iIndex < CellOriginals.size(); iIndex++) {
                if (Strings.isEmpty(CellOriginals.get(iIndex).getStrColumnName()))
                    continue;
                if (zhuBiao.contains(CellOriginals.get(iIndex).getStrColumnName()) == false) {
                    throw new defintException("正文数据的" + CellOriginals.get(iIndex).getStrColumnName()+ "不存在，请检查！");}
            }
        }
        //明细字段判断
        if (CellDetail.size() > 0) {
            if (data1 == null || data1.size() == 0) {
                throw new defintException("打印的明细数据不存在");
            }
            for (int iIndex = 0; iIndex < CellDetail.size(); iIndex++) {
                if (Strings.isEmpty(CellDetail.get(iIndex).getStrColumnName()))
                    continue;
                if (ziBiao.contains(CellDetail.get(iIndex).getStrColumnName()) == false) {
                    throw new defintException("明细数据的" + CellDetail.get(iIndex).getStrColumnName()+ "不存在，请检查！");}
            }
        }
        //分组一字段判断
        if (CellGroup.size() > 0) {
            if (group1 == null || group1.size() == 0) {
                throw new defintException("打印的一级分组数据不存在");
            }
            for (int iIndex = 0; iIndex < CellGroup.size(); iIndex++) {
                if (Strings.isEmpty(CellGroup.get(iIndex).getStrColumnName()))
                    continue;
                if (!CellGroup.get(iIndex).getStrColumnName().contains("()")
                        &&ziBiao.contains(CellGroup.get(iIndex).getStrColumnName()) == false) {
                    throw new defintException("一级分组的数据" + CellGroup.get(iIndex).getStrColumnName()+ "不存在，请检查！");}
            }
        }
        //分组二字段判断
        if (Cell2Group.size() > 0) {
            if (group2 == null || group2.size() == 0) {
                throw new defintException("打印的二级分组数据不存在");
            }
            for (int iIndex = 0; iIndex < Cell2Group.size(); iIndex++) {
                if (Strings.isEmpty(Cell2Group.get(iIndex).getStrColumnName()))
                    continue;
                if (!Cell2Group.get(iIndex).getStrColumnName().contains("「」")
                        &&ziBiao.contains(Cell2Group.get(iIndex).getStrColumnName()) == false) {
                    throw new defintException("二级分组的数据" + Cell2Group.get(iIndex).getStrColumnName()+ "不存在，请检查！");}
            }
        }
        //获取明细所在行数
        int startRow = getRownum(sheet, rowIndex,"<");
        //获取该行所占行数
        // int mergedRows = autoRowHeight.getMergedRows((XSSFRow) sheet.getRow(startRow));
        int mergedRows=1;
        //填充正文数据
        if (CellOriginals.size()>0) {
          /*  for (int rownum = 0; rownum < 50; rownum++) {
                Row row = sheet.getRow(rownum);
                if (row == null) continue;
                for (int cellNum = 0; cellNum < 30; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) continue;
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("[")) {
                        String substring = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int i = 0; i < customerfield.length; i++) {
                            boolean equals = customerfield[i].equals(substring);
                            if (equals) {
                                boolean flag = true;
                                if (cell.getCellComment() != null && cell.getCellComment().getString().toString().contains("图片")) {
                                    flag = false;
                                    cell.setCellValue("");
                                    int colIndex = cell.getColumnIndex();
                                    int rowNum = cell.getRow().getRowNum();
                                    insertPicture(sheet, cell, data1.get(i), colIndex, rowNum,
                                            colIndex + getMergeColumNum(cell, sheet), rowNum + getMergeRowNum(cell, sheet));
                                }
                                if (flag) cell.setCellValue(data1.get(i));
                         /*if (substring.equals("img")) {
                             cell.setCellValue("");
                             int colIndex = cell.getColumnIndex();
                             int rowNum = cell.getRow().getRowNum();
                             insertPicture(sheet,cell,data1.get(i), colIndex , rowNum,
                                     colIndex+getMergeColumNum(cell,sheet), rowNum+getMergeRowNum(cell,sheet));
                         }else {
                             cell.setCellValue(data1.get(i));}*//*
                            }
                        }
                    }
                }
            }*/
            for (int t=0;t<CellOriginals.size();t++){
                String strColumnName = CellOriginals.get(t).getStrColumnName();
                Cell cell = sheet.getRow(CellOriginals.get(t).getIRow()).getCell(CellOriginals.get(t).getICol());
                for (int i = 0; i < customerfield.length; i++) {
                    boolean equals = customerfield[i].equals(strColumnName);
                    if (equals) {
                        boolean flag = true;
                        if (cell.getCellComment() != null && cell.getCellComment().getString().toString().contains("图片")) {
                            flag = false;
                            cell.setCellValue("");
                            int colIndex = cell.getColumnIndex();
                            int rowNum = cell.getRow().getRowNum();
                            insertPicture(sheet, cell, data1.get(i), colIndex, rowNum,
                                    colIndex + getMergeColumNum(cell, sheet), rowNum + getMergeRowNum(cell, sheet));
                        }
                        if (flag) cell.setCellValue(data1.get(i));
                    }
                }
            }
        }
        //如果没有分组填充明细数据
        if (CellDetail.size()>0){
            /*for (int i=mergedRows-1;i< mergedRows*(data.size()-1);i+=mergedRows){
                //判断下一行是否为空，为空则直接复制，不为空则插入行
                if(!template2.isRowEmpty(sheet.getRow(startRow+i+1))|!template2.isRowEmpty(sheet.getRow(startRow+i+mergedRows))){
                  sheet.shiftRows(startRow+i+1,sheet.getLastRowNum(),mergedRows);
                    template2.copyRow(workBook,sheet.getRow(startRow),sheet.getRow(startRow+i+1),startRow+i+1);
                }else {
                    template2.copyRow(workBook,sheet.getRow(startRow),sheet.getRow(startRow+i+1),startRow+i+1);
                }
            }*/
            mergedRows=CellDetail.get(CellDetail.size()-1).getIRow()-CellDetail.get(0).getIRow()+1;
            for (int i=0;i< mergedRows*(data.size()-1);i+=mergedRows){
                //判断下一行是否为空，为空则直接复制，不为空则插入行
                   if(!template2.isRowEmpty(sheet.getRow(startRow+i+mergedRows))|!template2.isRowEmpty(sheet.getRow(startRow+i+2*mergedRows-1))){
                     sheet.shiftRows(startRow+i+mergedRows,sheet.getLastRowNum(),mergedRows);
                     template2.copyRow1(workBook,startRow,CellDetail.get(CellDetail.size()-1).getIRow(),CellDetail.get(CellDetail.size()-1).getIRow()+i+1);
                }else {
                     template2.copyRow1(workBook,startRow,CellDetail.get(CellDetail.size()-1).getIRow(),CellDetail.get(CellDetail.size()-1).getIRow()+i+1);
                }
            }
          /*  for (int rownum=startRow;rownum<50;rownum+=mergedRows,size++){
                Row row = sheet.getRow(rownum);
                if(row==null)    continue;
                for (int num=0;num<30;num++){
                    Cell cell= row.getCell(num);
                    if (cell==null)  continue;
                    String s = formatter.formatCellValue(cell);
                    if(s!=null&&s.contains("<")&&s.contains(">")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int i=0;i<productfiled.length;i++){
                            boolean equals = productfiled[i].equals(substrings);
                            if (equals){
                       boolean flag1=true;
                           if (cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("图片")){
                               flag1=false;
                           }
                       if (flag1){
                           if (data.size() == 0) cell.setCellValue("");
                           if (size < data.size()) cell.setCellValue(setValues(data.get(size), i));
                       }
                            }
                        }
                    }
                }
                autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) row);
             //   autoheight.cellAndSetRowHeigt((XSSFRow) row);
            }*/
            for (int x=0,j=0;x<data.size();x++,size++,j+=mergedRows) {
                Row row=null;
                for (int t = 0; t < CellDetail.size(); t++) {
                    String strColumnName = CellDetail.get(t).getStrColumnName();
                     row = sheet.getRow(CellDetail.get(t).getIRow()+j);
                     if (row==null) continue;
                    Cell cell = sheet.getRow(CellDetail.get(t).getIRow()+j).getCell(CellDetail.get(t).getICol());
                    if (cell==null) cell= row.createCell(CellDetail.get(t).getICol());
                    for (int i = 0; i < productfiled.length; i++) {
                        boolean equals = productfiled[i].equals(strColumnName);
                        if (equals) {
                            boolean flag1 = true;
                            if (cell.getCellComment() != null && cell.getCellComment().getString().toString().contains("图片")) {
                                flag1 = false;
                            }
                            if (flag1) {
                                if (data.size() == 0) cell.setCellValue("");
                                if (size < data.size()) cell.setCellValue(setValues(data.get(size), i));
                            }
                        }
                    }
                  if (CellDetail.get(t).getStrComment().contains("自动行高"))  autoRowHeight.cellAndSetRowHeigt(sheet, (XSSFRow) row);
                }
                  //  autoRowHeight.cellAndSetRowHeigt(sheet, (XSSFRow) row);
                //   autoheight.cellAndSetRowHeigt((XSSFRow) row);
            }
        }
        //如果要进行分组则执行以下代码，先进行一级分组再进行二级分组。
        //获取分组字段的位置
        for (int j=0;j<CellDetail.size();j++){
            if (CellDetail.get(j).getStrColumnName().contains(group1Column)) {
                cellGroupRow= CellDetail.get(j).getIRow();
                cellGroupcol= CellDetail.get(j).getICol();
            }
        }
        //二级分组有明细和一级分组
        if(Cell2Group.size()>0&&CellDetail.size()>0&&CellGroup.size()>0){
            List<Integer> notsame = group1(workBook, sheet, rowIndex, cellGroupRow, cellGroupcol, mergedRows, group1, group2, productfiled,data);
            int rownum = getRownum(sheet, rowIndex, Cell2Group.get(0).getStrColumnName());
            int rownum2=rownum+data.size()*mergedRows+group1.size()*(CellGroup.get(CellGroup.size()-1).getIRow()-CellGroup.get(0).getIRow()-mergedRows+1)+1;
            System.out.println("rownum2"+rownum2);
            //获得分组占几行
            int lines;
            int group1Row = CellGroup.get(CellGroup.size() - 1).getIRow() - CellGroup.get(0).getIRow() - mergedRows + 1;
            lines=Cell2Group.get(Cell2Group.size()-1).getIRow()-Cell2Group.get(0).getIRow()-mergedRows-group1Row+1;
            int diff=CellGroup.get(0).getIRow()-Cell2Group.get(0).getIRow();
            System.out.println(lines);
            System.out.println(diff);
            for (int i = 0,count=0; i < notsame.size(); i++, count += lines) {
                sheet.shiftRows(notsame.get(i) + count, sheet.getLastRowNum(), lines);
                for (int n=0,m=0;n<diff;n++,m++){
                    if (sheet.getRow(notsame.get(i) + count + lines-diff+m) == null)
                        sheet.createRow(notsame.get(i) + count + lines-diff+m);
                }
                for (int n = 0, m = 0; n < lines - diff; n++, m++) {
                    if (sheet.getRow(notsame.get(i) + count + m) == null) sheet.createRow(notsame.get(i) + count+m);
                }
               /* for (int n = 0, m = 0; n < lines - 1; n++, m++) {
                    template2.copyRow(workBook, sheet.getRow(rownum2 + count + lines + m), sheet.getRow(notsame.get(i) + count + m), notsame.get(i) + count + m);
                }
                    template2.copyRow(workBook, sheet.getRow(rownum), sheet.getRow(notsame.get(i) + count + lines - 1), notsame.get(i) + count + lines - 1);*/
                template2.copyRow1(workBook, rownum2 + count + lines +diff-1,rownum2 + count + lines +lines-diff-1, notsame.get(i) + count );
                template2.copyRow1(workBook, rownum,rownum+diff-1, notsame.get(i) + count + lines - diff);
               for (int temp=0;temp<diff;temp++){
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + lines - diff+temp).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("「") && s.contains("」")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int n = 0; n < productfiled.length; n++) {
                            boolean equals = productfiled[n].equals(substrings);
                            if (group2.size() > 0 && equals) {
                                if (i + 1 < group2.size()) cell.setCellValue(setValues(group2.get(i + 1), n));
                            }
                        }
                    }
                }
               }
                for (int j = 0; j < lines-1; j++){
                    for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count+j).cellIterator(); cellIterator.hasNext(); ) {
                        Cell cell = cellIterator.next();
                        String s = formatter.formatCellValue(cell);
                        if (s != null && s.contains("「") && s.contains("」")) {
                            String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                            if (substrings.length()==0){
                                cell.setCellValue("");
                                continue;
                            }
                            for (int n = 0; n < productfiled.length; n++) {
                                boolean equals = productfiled[n].equals(substrings);
                                if (equals) {
                                    cell.setCellValue(setValues(group2.get(i), n));
                                }
                            }
                        }
                    }
                }
            }
           for (int temp=0;temp<diff;temp++){
            for (Iterator<Cell> cellIterator = sheet.getRow(rownum+temp).cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                String s = formatter.formatCellValue(cell);
                if (s!=null&&s.contains("「")&&s.contains("」")) {
                    String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                    for (int n=0;n<productfiled.length;n++){
                        boolean equals = productfiled[n].equals(substrings);
                        if (group2.size()>0&&equals) {
                            cell.setCellValue(setValues(group2.get(0),n));
                        }
                    }
                }
            }
           }
            for (int v=0;v<Cell2Group.size();v++) {
                for (Iterator<Cell> cellIterator = sheet.getRow(getRownum(sheet, rowIndex,
                        Cell2Group.get(v).getStrColumnName())).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("「") && s.contains("」")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        if (substrings.length() == 0) {
                            cell.setCellValue("");
                            continue;
                        }
                        for (int n = 0; n < productfiled.length; n++) {
                            boolean equals = productfiled[n].equals(substrings);
                            if (group2.size() > 0 && equals) {
                                cell.setCellValue(setValues(group2.get(group2.size() - 1), n));
                            }
                        }
                    }
                }
            }
        }
        //一级分组有明细
        if(CellGroup.size()>0&&CellDetail.size()>0&&Cell2Group.size()==0){
            int offSet=data.size()*mergedRows;
            int rownum = getRownum(sheet, rowIndex, CellGroup.get(0).getStrColumnName());
            List<Integer> notsame = notsame(sheet, cellGroupRow, cellGroupcol,mergedRows);
            System.out.println(notsame);
            System.out.println(CellGroup);
            //计算一级分组有几行
            int lines=1;
            lines=  CellGroup.get(CellGroup.size()-1).getIRow()-CellGroup.get(0).getIRow()-mergedRows+1;
            int diff=CellDetail.get(0).getIRow()-CellGroup.get(0).getIRow();
            System.out.println(diff);
            for (int i = 0,count=0; i < notsame.size(); i++, count += lines) {
                sheet.shiftRows(notsame.get(i) + count, sheet.getLastRowNum(), lines);
                //判断行是否存在，如果不存在则创建该行。
                for (int n=0,m=0;n<diff;n++,m++){
                if (sheet.getRow(notsame.get(i) + count + lines-diff+m) == null)
                    sheet.createRow(notsame.get(i) + count + lines-diff+m);
                }
                for (int n=0,m=0;n<lines-diff;n++,m++) {
                    if (sheet.getRow(notsame.get(i) + count + m) == null) sheet.createRow(notsame.get(i) + count + m);
                }
                //复制分组行
               /* for (int n=0,m=0;n<lines-1;n++,m++){
                    template2.copyRow(workBook, sheet.getRow(rownum+offSet + count + lines+m+1), sheet.getRow(notsame.get(i) + count+m), notsame.get(i) + count+m);
                }
              //  template2.copyRow(workBook, sheet.getRow(rownum), sheet.getRow(notsame.get(i) + count + lines-1), notsame.get(i) + count + lines-1);
                */
                //复制分组行
                template2.copyRow1(workBook,rownum+offSet + count + lines+diff,rownum+offSet + count + lines+lines-diff, notsame.get(i) + count);
                template2.copyRow1(workBook, rownum,rownum+diff-1,notsame.get(i) + count + lines-diff);
              for (int temp=0;temp<diff;temp++){
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + lines-diff+temp).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s!=null&&s.contains("(")&&s.contains(")")){
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int n=0;n<productfiled.length;n++){
                            boolean equals = productfiled[n].equals(substrings);
                            if (group1.size()>0&&equals) {
                                if (i + 1 < group1.size())  cell.setCellValue(setValues(group1.get(i+1),n));
                            }
                        }
                    }
                    if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                        autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(notsame.get(i) + count + lines-diff+temp));
                    }
                }
              }
                for (int j=0;j<lines-1;j++){
                  for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count+j).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s!=null&&s.contains("(")&&s.contains(")")){
                    String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                    if (substrings.length()==0) {
                        cell.setCellValue("");
                        continue;
                    }
                      for (int n=0;n<productfiled.length;n++){
                        boolean equals = productfiled[n].equals(substrings);
                        if (equals) {
                            cell.setCellValue(setValues(group1.get(i),n));
                        }
                      }
                    }
                  }
                }
            }
            for (int temp=0;temp<diff;temp++) {
                for (Iterator<Cell> cellIterator = sheet.getRow(rownum+temp).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("(") && s.contains(")")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int n = 0; n < productfiled.length; n++) {
                            boolean equals = productfiled[n].equals(substrings);
                            if (group1.size() > 0 && equals) {
                                cell.setCellValue(setValues(group1.get(0), n));
                            }
                        }
                    }
                    if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                        autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(rownum+temp));
                    }
                }
            }
            for (int j=0;j<CellGroup.size();j++){
            for (Iterator<Cell> cellIterator = sheet.getRow( getRownum(sheet, rowIndex,
                    CellGroup.get(j).getStrColumnName())).cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                String s = formatter.formatCellValue(cell);
                if (s!=null&&s.contains("(")&&s.contains(")")){
                    String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                    if (substrings.length()==0) {
                        cell.setCellValue("");
                        continue;
                    }
                    for (int n=0;n<productfiled.length;n++){
                        boolean equals = productfiled[n].equals(substrings);
                        if (group1.size()>0&&equals) {
                            cell.setCellValue(setValues(group1.get(group1.size()-1),n));
                        }
                    }
                }
            }
            }
        }
        //没明细只有一级分组
        if(CellGroup.size()>0&&CellDetail.size()==0&&Cell2Group.size()==0){
          int offSet=CellGroup.get(CellGroup.size()-1).getIRow()-CellGroup.get(0).getIRow()+1;
          int start=CellGroup.get(0).getIRow();
          int end=CellGroup.get(CellGroup.size()-1).getIRow();
        for (int i=0,m=0;i<group1.size()-1;i++,m+=offSet){
            if(!template2.isRowEmpty(sheet.getRow(start+m+offSet))|!template2.isRowEmpty(sheet.getRow(start+m+2*offSet-1))){
                sheet.shiftRows(start+m+offSet,sheet.getLastRowNum(),offSet);
                template2.copyRow1(workBook,start,end,end+m+1);
            }else {
                template2.copyRow1(workBook,start,end,end+m+1);
            }
        }
            for (int x=0,j=0;x<group1.size();x++,size++,j+=offSet) {
                Row row=null;
                for (int t = 0; t < CellGroup.size(); t++) {
                    String strColumnName = CellGroup.get(t).getStrColumnName();
                    row = sheet.getRow(CellGroup.get(t).getIRow()+j);
                    if (row==null) continue;
                    Cell cell = sheet.getRow(CellGroup.get(t).getIRow()+j).getCell(CellGroup.get(t).getICol());
                    if (cell==null) cell= row.createCell(CellGroup.get(t).getICol());
                    if (strColumnName.contains("(")&&strColumnName.contains(")")) {
                        cell.setCellValue("");
                        continue;
                    }
                    for (int i = 0; i < productfiled.length; i++) {
                        boolean equals = productfiled[i].equals(strColumnName);
                        if (equals) {
                                if (group1.size() == 0) cell.setCellValue("");
                                if (size < group1.size()) cell.setCellValue(setValues(group1.get(size), i));
                        }
                    }
                    if (CellGroup.get(t).getStrComment().contains("自动行高"))  autoRowHeight.cellAndSetRowHeigt(sheet, (XSSFRow) row);
                }
            }
        }
        //无明细和一级分组，只有二级分组
        if(Cell2Group.size()>0&&CellDetail.size()==0&&CellGroup.size()==0){
            int offSet=Cell2Group.get(Cell2Group.size()-1).getIRow()-Cell2Group.get(0).getIRow()+1;
            int start=Cell2Group.get(0).getIRow();
            int end=Cell2Group.get(Cell2Group.size()-1).getIRow();
            for (int i=0,m=0;i<group2.size()-1;i++,m+=offSet){
                if(!template2.isRowEmpty(sheet.getRow(start+m+offSet))|!template2.isRowEmpty(sheet.getRow(start+m+2*offSet-1))){
                    sheet.shiftRows(start+m+offSet,sheet.getLastRowNum(),offSet);
                    template2.copyRow1(workBook,start,end,end+m+1);
                }else {
                    template2.copyRow1(workBook,start,end,end+m+1);
                }
            }
            for (int x=0,j=0;x<group2.size();x++,size++,j+=offSet) {
                Row row=null;
                for (int t = 0; t < Cell2Group.size(); t++) {
                    String strColumnName = Cell2Group.get(t).getStrColumnName();
                    row = sheet.getRow(Cell2Group.get(t).getIRow()+j);
                    if (row==null) continue;
                    Cell cell = sheet.getRow(Cell2Group.get(t).getIRow()+j).getCell(Cell2Group.get(t).getICol());
                    if (cell==null) cell= row.createCell(Cell2Group.get(t).getICol());
                    if (strColumnName.contains("「")&&strColumnName.contains("」")) {
                        cell.setCellValue("");
                        continue;
                    }
                    for (int i = 0; i < productfiled.length; i++) {
                        boolean equals = productfiled[i].equals(strColumnName);
                        if (equals) {
                            if (group2.size() == 0) cell.setCellValue("");
                            if (size < group2.size()) cell.setCellValue(setValues(group2.get(size), i));
                        }
                    }
                    if (Cell2Group.get(t).getStrComment().contains("自动行高"))  autoRowHeight.cellAndSetRowHeigt(sheet, (XSSFRow) row);
                }
            }
        }
        //无明显有一级分组和二级分组
        if(CellGroup.size()>0&&CellDetail.size()==0&&Cell2Group.size()>0){
            List<Integer> notsame = group2(workBook, group1, group2, productfiled);
            System.out.println(notsame);
            int rownum = getRownum(sheet, rowIndex, Cell2Group.get(0).getStrColumnName());
            int rownum2=rownum+group1.size()*(CellGroup.get(CellGroup.size()-1).getIRow()-CellGroup.get(0).getIRow()+1)+1;
            int lines;
            int group1Row = CellGroup.get(CellGroup.size() - 1).getIRow() - CellGroup.get(0).getIRow()  + 1;
            lines=Cell2Group.get(Cell2Group.size()-1).getIRow()-Cell2Group.get(0).getIRow()-group1Row+1;
            int diff=CellGroup.get(0).getIRow()-Cell2Group.get(0).getIRow();
            System.out.println(lines);
            System.out.println(diff);
            for (int i = 0,count=0; i < notsame.size(); i++, count += lines) {
                sheet.shiftRows(notsame.get(i) + count, sheet.getLastRowNum(), lines);
                for (int n = 0, m = 0; n < diff; n++, m++) {
                    if (sheet.getRow(notsame.get(i) + count + lines - diff + m) == null)
                        sheet.createRow(notsame.get(i) + count + lines - diff + m);
                }
                for (int n = 0, m = 0; n < lines - diff; n++, m++) {
                    if (sheet.getRow(notsame.get(i) + count + m) == null) sheet.createRow(notsame.get(i) + count + m);
                }
                template2.copyRow1(workBook, rownum2 + count + lines + diff - 1, rownum2 + count + lines + lines - diff - 1, notsame.get(i) + count);
                template2.copyRow1(workBook, rownum, rownum + diff - 1, notsame.get(i) + count + lines - diff);
                for (int temp=0;temp<diff;temp++){
                    for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + lines - diff+temp).cellIterator(); cellIterator.hasNext(); ) {
                        Cell cell = cellIterator.next();
                        String s = formatter.formatCellValue(cell);
                        if (s != null && s.contains("「") && s.contains("」")) {
                            String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                            for (int n = 0; n < productfiled.length; n++) {
                                boolean equals = productfiled[n].equals(substrings);
                                if (group2.size() > 0 && equals) {
                                    if (i + 1 < group2.size()) cell.setCellValue(setValues(group2.get(i + 1), n));
                                }
                            }
                        }
                        if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                            autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(notsame.get(i) + count + lines-diff+temp));
                        }
                    }
                }
                for (int j = 0; j < lines-1; j++){
                    for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count+j).cellIterator(); cellIterator.hasNext(); ) {
                        Cell cell = cellIterator.next();
                        String s = formatter.formatCellValue(cell);
                        if (s != null && s.contains("「") && s.contains("」")) {
                            String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                            if (substrings.length()==0){
                                cell.setCellValue("");
                                continue;
                            }
                            for (int n = 0; n < productfiled.length; n++) {
                                boolean equals = productfiled[n].equals(substrings);
                                if (equals) {
                                    cell.setCellValue(setValues(group2.get(i), n));
                                }
                            }
                        }
                    }
                }
            }
            for (int temp=0;temp<diff;temp++){
                for (Iterator<Cell> cellIterator = sheet.getRow(rownum+temp).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s!=null&&s.contains("「")&&s.contains("」")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        for (int n=0;n<productfiled.length;n++){
                            boolean equals = productfiled[n].equals(substrings);
                            if (group2.size()>0&&equals) {
                                cell.setCellValue(setValues(group2.get(0),n));
                            }
                        }
                    }
                    if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                        autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(rownum+temp));
                    }
                }
            }
            for (int v=0;v<Cell2Group.size();v++) {
                for (Iterator<Cell> cellIterator = sheet.getRow(getRownum(sheet, rowIndex,
                        Cell2Group.get(v).getStrColumnName())).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("「") && s.contains("」")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        if (substrings.length() == 0) {
                            cell.setCellValue("");
                            continue;
                        }
                        for (int n = 0; n < productfiled.length; n++) {
                            boolean equals = productfiled[n].equals(substrings);
                            if (group2.size() > 0 && equals) {
                                cell.setCellValue(setValues(group2.get(group2.size() - 1), n));
                            }
                        }
                    }
                }
            }
        }
        //向下合并单元格
        mergeDown(sheet);
        //重新插入图片
        restartinsert(startRow,sheet,formatter,productfiled,data);
        //开启公式计算
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
   //获得数据库表中字段的名称
    @Override
    public  String[] fieldName1(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName();
        }
        return fieldNames;
    }
    //加了静态方法获取实体类中字段名
    public static String[] fieldName(Class clazz) {
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
    //生成新的文件并重新命名
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
    //设置数据库对应的值
    public static String setValues(product product,int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       String[] fieldNames = fieldName(product.class);
           String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
           Method method = product.getClass().getMethod(methodName);
       return method.invoke(product).toString();
   }
    //通过内容得到某个单元格的所在行
    public  static int getRownum(Sheet sheet,int Rownum ,String s1){
        int rowindex=1;
        DataFormatter formatter = new DataFormatter();
        for (int rownum=0;rownum<sheet.getLastRowNum();rownum++){
            Row row = sheet.getRow(rownum);
            if(row==null) continue;
            for (int cellNum=0;cellNum<30;cellNum++){
                Cell cell= row.getCell(cellNum);
                if (cell==null)  continue;
                String s = formatter.formatCellValue(cell);
                if (s!=null&&s1.length()!=0&&s.contains(s1)) {
                    if (Rownum==rowindex) {
                        Rownum = cell.getRowIndex();
                        return Rownum;
                    }
                      rowindex++;
                }
            }
        }
        return  Rownum;
    }
    //得到每一行数据中分组的那一列与下一行不同的行存储在List列表中
    public  static List<Integer> notsame(Sheet sheet ,int startRow,int col,int merginRows){
        DataFormatter formatter = new DataFormatter();
        ArrayList<Integer> list = new ArrayList<>();
          for (int i=0;i<sheet.getLastRowNum();i++){
              String value="";
              String value1="";
              if (sheet.getRow(startRow+i)!=null&&sheet.getRow(startRow+i+merginRows)!=null){
               value = formatter.formatCellValue(sheet.getRow(startRow + i).getCell(col)) ;
               value1 =formatter.formatCellValue(sheet.getRow(startRow + i+merginRows).getCell(col));
              }
              if(!value.equals(value1)){
                  if (value==null|value1==null|value1==""|value=="") break;
                  list.add(startRow+i+merginRows);
              }
          }
        return list;
    }
    //插入图片
    public static void insertPicture(Sheet sheet1,Cell cell,String img,int col1,int row1,int col2,int row2){
        BufferedImage bufferImg = null;
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File(img));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            XSSFDrawing shapes =(XSSFDrawing)sheet1.createDrawingPatriarch();
            ClientAnchor anchor=null;
            int   sum3=0;
            float widths=0;
            float heights=0;
            if (template2.isMergedRegion(sheet1,row1,col1)){
                getMergeColumNum(cell,sheet1);
                for (int i=0;i<getMergeColumNum(cell,sheet1);i++){
                   widths+=sheet1.getColumnWidthInPixels(cell.getColumnIndex()+i);
                }
                for (int i=0;i<getMergeRowNum(cell,sheet1);i++){
                    if(sheet1.getRow(cell.getRowIndex() +i)!=null){
                    heights+=sheet1.getRow(cell.getRowIndex() +i).getHeightInPoints();
                    }
                }
                double w=heights*bufferImg.getWidth() / bufferImg.getHeight();
                 sum3=(int)(widths-w)/2;
            }else {
                   widths = sheet1.getColumnWidthInPixels(cell.getColumnIndex());
                   heights = cell.getRow().getHeightInPoints();
                double wid=heights*bufferImg.getWidth() / bufferImg.getHeight();
                 sum3=(int)(widths-wid)/2;
            }
           if (sum3<0) {
               System.out.println("差值"+sum3+"和宽"+widths+"和高"+heights);
               anchor = new XSSFClientAnchor(EMU_PER_PIXEL*5, -EMU_PER_PIXEL*sum3,-EMU_PER_PIXEL*5 ,EMU_PER_PIXEL*sum3,
                       (short) col1, row1, (short) col2, row2);
           }else if(heights>widths){
               anchor = new XSSFClientAnchor(EMU_PER_PIXEL*5, EMU_PER_PIXEL*sum3,-EMU_PER_PIXEL*5 ,-EMU_PER_PIXEL*sum3,
                       (short) col1, row1, (short) col2, row2);
           } else {
               anchor = new XSSFClientAnchor(EMU_PER_PIXEL*sum3, EMU_PER_PIXEL*2, -EMU_PER_PIXEL*sum3, -EMU_PER_PIXEL*2,
                       (short) col1, row1, (short) col2, row2);
           }
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            XSSFPicture picture = shapes.createPicture(anchor, sheet1.getWorkbook().addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
            picture.resize(1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //得到某个单元格合并的行数
    public static int getMergeRowNum(Cell cell, Sheet sheet) {
        int mergeSize = 1;
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress cellRangeAddress : mergedRegions) {
            if (cellRangeAddress.isInRange(cell)) {
                mergeSize = cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow() + 1;
                break;
            }
        }
        return mergeSize;
    }
    //得到合并单元格合并的列数
    public static int getMergeColumNum(Cell cell, Sheet sheet) {
        int mergeSize = 1;
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress cellRangeAddress : mergedRegions) {
            if (cellRangeAddress.isInRange(cell)) {
                //获取合并的列数
                mergeSize = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
                break;
            }
        }
        return mergeSize;
    }
    //最后重新插入图片
    public  void restartinsert(int startRow,Sheet sheet,DataFormatter formatter
    ,String[] productfiled,List<product> data) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int size=0;
        for (int rownum=startRow;rownum<sheet.getLastRowNum();rownum++){
            Row row = sheet.getRow(rownum);
            if(row==null)  continue;
            for (int num=0;num<30;num++){
                Cell cell= row.getCell(num);
                if (cell==null)  continue;
                String s = formatter.formatCellValue(cell);
                if(s!=null&&s.contains("<")) {
                    String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                    for (int i=0;i<productfiled.length;i++){
                        boolean equals = productfiled[i].equals(substrings);
                        if (equals){
                            if (cell.getCellComment()!=null&& cell.getCellComment().
                                    getString().toString().contains("图片")){
                                cell.setCellValue("");
                                int colIndex = cell.getColumnIndex();
                                int rowNum = cell.getRow().getRowNum();
                                if(data.size()!=0&&size<data.size())  insertPicture(sheet,cell, setValues(data.get(size),i), colIndex , rowNum , colIndex+1, rowNum+1);
                                size++;
                            }

                           /* if (substrings.equals("pic")){
                                cell.setCellValue("");
                                int colIndex = cell.getColumnIndex();
                                int rowNum = cell.getRow().getRowNum();
                                if(data.size()!=0&&size<data.size())  insertPicture(sheet,cell, setValues(data.get(size),i), colIndex , rowNum , colIndex+1, rowNum+1);
                                size++;
                            }*/
                        }
                    }
                }
            }
        }
    }
    //合并单元格操作
    public static void mergeRegin(Sheet sheet,int up,int down,int l,int r){
        sheet.addMergedRegion(new CellRangeAddress(up,down,l,r));
    }

    //判断向下合并单元格
    public static void mergeDown(Sheet sheet){
        Cell testcell=null;
    if (CellSuspend.size()>0){
        if (sheet.getRow(CellSuspend.get(0).getIRow())!=null){
            testcell = sheet.getRow(CellSuspend.get(0).getIRow()).getCell(CellSuspend.get(0).getICol());
        }
      DataFormatter formatter = new DataFormatter();
      testcell.setCellValue("");
      int rowIndex1 = testcell.getRowIndex();
      int columnIndex = testcell.getColumnIndex();
      int i=1;
      while (sheet.getRow(rowIndex1+i)==null|(sheet.getRow(rowIndex1+i)!=null&&(sheet.getRow(rowIndex1+i).getCell(columnIndex)==null)
      |formatter.formatCellValue(sheet.getRow(rowIndex1+i).getCell(columnIndex))=="")){
          if (template2.isMergedRegion(sheet,rowIndex1,columnIndex)){
              int sheetMergeCount = sheet.getNumMergedRegions();
             // int m=0;
              for (int n = 0; n< sheetMergeCount; n++) {
                  CellRangeAddress ca=sheet.getMergedRegion(n);
                  if(testcell.getRowIndex() >= ca.getFirstRow() && testcell.getRowIndex() <= ca.getLastRow()) {
                      if(testcell.getColumnIndex() >= ca.getFirstColumn() && testcell.getColumnIndex() <= ca.getLastColumn()) {
                         // m=n;
                          sheet.removeMergedRegion(n);
                      }
                  }
              }
          }
          mergeRegin(sheet,rowIndex1,rowIndex1+i,columnIndex,columnIndex);
          i++;
      }
    }
  }
    //封装了无明细的一级分组
    public  static  List<Integer> group2(Workbook workBook,List<product> group1,List<product> group2,String[] productfiled) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
      Sheet sheet = workBook.getSheetAt(0);
      int offSet=CellGroup.get(CellGroup.size()-1).getIRow()-CellGroup.get(0).getIRow()+1;
      int start=CellGroup.get(0).getIRow();
      int end=CellGroup.get(CellGroup.size()-1).getIRow();
      List<Integer> list = new ArrayList<>();
      for (int i=0,m=0,line=0,sum=0;i<group1.size()-1;i++,m+=offSet){
          if(!template2.isRowEmpty(sheet.getRow(start+m+offSet))|!template2.isRowEmpty(sheet.getRow(start+m+2*offSet-1))){
              sheet.shiftRows(start+m+offSet,sheet.getLastRowNum(),offSet);
              template2.copyRow1(workBook,start,end,end+m+1);
          }else {
              template2.copyRow1(workBook,start,end,end+m+1);
          }
          if (line < group2.size() - 1 && i == group2.get(line).getCount1() - 1+sum) {
              sum=sum+group2.get(line).getCount1();
              list.add(end+m+1);
              line++;
          }
      }
      int size=0;
      for (int x=0,j=0;x<group1.size();x++,size++,j+=offSet) {
          Row row=null;
          for (int t = 0; t < CellGroup.size(); t++) {
              String strColumnName = CellGroup.get(t).getStrColumnName();
              row = sheet.getRow(CellGroup.get(t).getIRow()+j);
              if (row==null) continue;
              Cell cell = sheet.getRow(CellGroup.get(t).getIRow()+j).getCell(CellGroup.get(t).getICol());
              if (cell==null) cell= row.createCell(CellGroup.get(t).getICol());
              if (strColumnName.contains("(")&&strColumnName.contains(")")) {
                  cell.setCellValue("");
                  continue;
              }
              for (int i = 0; i < productfiled.length; i++) {
                  boolean equals = productfiled[i].equals(strColumnName);
                  if (equals) {
                      if (group1.size() == 0) cell.setCellValue("");
                      if (size < group1.size()) cell.setCellValue(setValues(group1.get(size), i));
                  }
              }
              if (CellGroup.get(t).getStrComment().contains("自动行高"))  autoRowHeight.cellAndSetRowHeigt(sheet, (XSSFRow) row);
          }
      }
      return list;
  }

    //封装了有明细的一级分组
    public  static List<Integer> group1(Workbook workBook,Sheet sheet,int rowIndex,int cellGroupRow,int cellGroupcol,
                               int mergedRows, List<product> group1 ,List<product> group2,String[] productfiled,List<product> data) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        DataFormatter formatter = new DataFormatter();
        int offSet = data.size() * mergedRows;
        int rownum = getRownum(sheet, rowIndex, CellGroup.get(0).getStrColumnName());
        List<Integer> notsame = notsame(sheet, cellGroupRow, cellGroupcol, mergedRows);
        System.out.println(notsame);
        List<Integer> list = new ArrayList<>();
        int lines ;
        lines = CellGroup.get(CellGroup.size() - 1).getIRow() - CellGroup.get(0).getIRow() - mergedRows + 1;
        int diff=CellDetail.get(0).getIRow()-CellGroup.get(0).getIRow();
        System.out.println(diff);
        for (int i = 0, count = 0, line = 0,sum=0; i < notsame.size(); i++, count += lines) {
            sheet.shiftRows(notsame.get(i) + count, sheet.getLastRowNum(), lines);
            for (int n=0,m=0;n<diff;n++,m++){
                if (sheet.getRow(notsame.get(i) + count + lines - diff+m) == null)
                    sheet.createRow(notsame.get(i) + count + lines - diff+m);
            }
            for (int n=0,m=0;n<lines-diff;n++,m++) {
                if (sheet.getRow(notsame.get(i) + count+m) == null) sheet.createRow(notsame.get(i) + count+m);
            }
           /* for (int n = 0, m = 0; n < lines - 1; n++, m++) {
               template2.copyRow(workBook, sheet.getRow(rownum + offSet + count + lines + m + 1), sheet.getRow(notsame.get(i) + count + m), notsame.get(i) + count + m);
            }
            template2.copyRow(workBook, sheet.getRow(rownum), sheet.getRow(notsame.get(i) + count + lines - 1), notsame.get(i) + count + lines - 1);*/
            //分组1合计
           template2.copyRow1(workBook, rownum + offSet + count + lines+diff, rownum+offSet + count + lines+lines-diff, notsame.get(i) + count );
           template2.copyRow1(workBook, rownum,rownum+diff-1,notsame.get(i) + count + lines - diff);
            //获取分组二要插入的行数
            if (line < group2.size() - 1 && i== group2.get(line).getCount1() - 1+sum) {
                sum=sum+group2.get(line).getCount1();
                list.add(notsame.get(i) + count + lines - diff);
                line++;
            }
          for (int temp=0;temp<diff;temp++){
            for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + lines - diff+temp).cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                String s = formatter.formatCellValue(cell);
                if (s != null && s.contains("(") && s.contains(")")) {
                    String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                    for (int n = 0; n < productfiled.length; n++) {
                        boolean equals = productfiled[n].equals(substrings);
                        if (group1.size() > 0 && equals) {
                            if (i + 1 < group1.size()) cell.setCellValue(setValues(group1.get(i + 1), n));
                        }
                    }
                }
                if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                    autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(notsame.get(i) + count + lines-diff+temp));
                }
            }
          }
            for (int j = 0; j < lines - 1; j++) {
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + j).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s != null && s.contains("(") && s.contains(")")) {
                        String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                        if (substrings.length() == 0) {
                            cell.setCellValue("");
                            continue;
                        }
                        for (int n = 0; n < productfiled.length; n++) {
                            boolean equals = productfiled[n].equals(substrings);
                            if (equals) {
                                cell.setCellValue(setValues(group1.get(i), n));
                            }
                        }
                    }
                }
            }
        }
       for (int temp=0;temp<diff;temp++){
        for (Iterator<Cell> cellIterator = sheet.getRow(rownum+temp).cellIterator(); cellIterator.hasNext(); ) {
            Cell cell = cellIterator.next();
            String s = formatter.formatCellValue(cell);
            if (s != null && s.contains("(") && s.contains(")")) {
                String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                for (int n = 0; n < productfiled.length; n++) {
                    boolean equals = productfiled[n].equals(substrings);
                    if (group1.size() > 0 && equals) {
                        cell.setCellValue(setValues(group1.get(0), n));
                    }
                }
            }
            if(cell.getCellComment()!=null&&cell.getCellComment().getString().toString().contains("自动行高")) {
                autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) sheet.getRow(rownum+temp));
            }
        }
       }

        for (int j=0;j<CellGroup.size();j++){
        for (Iterator<Cell> cellIterator = sheet.getRow(getRownum(sheet, rowIndex,
                CellGroup.get(j).getStrColumnName())).cellIterator(); cellIterator.hasNext(); ) {
            Cell cell = cellIterator.next();
            String s = formatter.formatCellValue(cell);
            if (s != null && s.contains("(") && s.contains(")")) {
                String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
                if (substrings.length() == 0) {
                    cell.setCellValue("");
                    continue;
                }
                for (int n = 0; n < productfiled.length; n++) {
                    boolean equals = productfiled[n].equals(substrings);
                    if (group1.size() > 0 && equals) {
                        cell.setCellValue(setValues(group1.get(group1.size() - 1), n));
                    }
                }
            }
        }
        }
        return list;
    }
    //遍历数组来存储模板数据
    public  static void initCell(Sheet sheet,int MulNums,boolean MulDetail){
        cellOriginal struCellCurrent;
        DataFormatter formatter = new DataFormatter();
        //开始读取数据
        CellOriginals = new ArrayList<>();
        //明细数据单元格的数组
        CellDetail = new ArrayList<>();
        //浮动数据单元格的数组
        CellSuspend = new ArrayList<>();
        //分组数据单元格的数组
        CellGroup = new ArrayList<>();
        //+1级分组数据单元格的数组
        Cell2Group = new ArrayList<>();
        System.out.println("总共几行"+sheet.getLastRowNum());
        for (int row=0;row<sheet.getLastRowNum();row++){
            Row row1 = sheet.getRow(row);
            if (row1==null) continue;
            for (int col=0;col<30;col++){
                Cell cell = row1.getCell(col);
                if (cell==null) continue;
                String strComment = cell.getCellComment() == null ? "" : cell.getCellComment().getString().getString();
                if (strComment == null)  strComment = "";
                String strColumnName = "";
               strColumnName=formatter.formatCellValue(cell);
               if (strColumnName==""|strColumnName==null) continue;
                struCellCurrent = new cellOriginal();
                //多明细
                if (MulDetail){
                    //正文
                    if (strColumnName.startsWith("["+MulNums+"")&&strColumnName.endsWith("]")){
                        struCellCurrent.setIRow(row);
                        struCellCurrent.setICol(col);
                        struCellCurrent.setStrColumnName(strColumnName.substring(2, strColumnName.length() - 1));
                        struCellCurrent.setStrComment(strComment);
                        CellOriginals.add(struCellCurrent);
                    }
                    //明细
                    if (strColumnName.startsWith("<"+MulNums+"")&&strColumnName.endsWith(">")){
                        struCellCurrent.setIRow(row);
                        struCellCurrent.setICol(col);
                        struCellCurrent.setStrColumnName(strColumnName.substring(2, strColumnName.length() - 1));
                        struCellCurrent.setStrComment(strComment);
                        CellDetail.add(struCellCurrent);
                    }
                    //唛头
                    if (strColumnName.startsWith("{"+MulNums+"")&&strColumnName.endsWith("}")){
                        struCellCurrent.setIRow(row);
                        struCellCurrent.setICol(col);
                        struCellCurrent.setStrColumnName(strColumnName.substring(2, strColumnName.length() - 1));
                        struCellCurrent.setStrComment(strComment);
                        CellSuspend.add(struCellCurrent);
                    }
                    //一级分组
                    if (strColumnName.startsWith("("+MulNums+"")&&strColumnName.endsWith(")")){
                        struCellCurrent.setIRow(row);
                        struCellCurrent.setICol(col);
                        if (strColumnName.length()==2){
                            struCellCurrent.setStrColumnName(strColumnName);
                        }else {
                            struCellCurrent.setStrColumnName(strColumnName.substring(2, strColumnName.length() - 1));
                        }
                        struCellCurrent.setStrComment(strComment);
                        CellGroup.add(struCellCurrent);
                    }

                    //二级分组
                    if (strColumnName.startsWith("「"+MulNums+"")&&strColumnName.endsWith("」")){
                        struCellCurrent.setIRow(row);
                        struCellCurrent.setICol(col);
                        if (strColumnName.length()==2){
                            struCellCurrent.setStrColumnName(strColumnName);
                        }else {
                            struCellCurrent.setStrColumnName(strColumnName.substring(2, strColumnName.length() - 1));
                        }
                        struCellCurrent.setStrComment(strComment);
                        Cell2Group.add(struCellCurrent);
                    }
                }else {
                //正文
                if (strColumnName.startsWith("[")&&strColumnName.endsWith("]")){
                    struCellCurrent.setIRow(row);
                    struCellCurrent.setICol(col);
                    struCellCurrent.setStrColumnName(strColumnName.substring(1, strColumnName.length() - 1));
                    struCellCurrent.setStrComment(strComment);
                    CellOriginals.add(struCellCurrent);
                }
                //明细
                if (strColumnName.startsWith("<")&&strColumnName.endsWith(">")){
                    struCellCurrent.setIRow(row);
                    struCellCurrent.setICol(col);
                    struCellCurrent.setStrColumnName(strColumnName.substring(1, strColumnName.length() - 1));
                    struCellCurrent.setStrComment(strComment);
                    CellDetail.add(struCellCurrent);
                }
                //唛头
                if (strColumnName.startsWith("{")&&strColumnName.endsWith("}")){
                    struCellCurrent.setIRow(row);
                    struCellCurrent.setICol(col);
                    struCellCurrent.setStrColumnName(strColumnName.substring(1, strColumnName.length() - 1));
                    struCellCurrent.setStrComment(strComment);
                    CellSuspend.add(struCellCurrent);
                }
                //一级分组
                if (strColumnName.startsWith("(")&&strColumnName.endsWith(")")){
                    struCellCurrent.setIRow(row);
                    struCellCurrent.setICol(col);
                    if (strColumnName.length()==2){
                        struCellCurrent.setStrColumnName(strColumnName);
                    }else {
                        struCellCurrent.setStrColumnName(strColumnName.substring(1, strColumnName.length() - 1));
                    }
                    struCellCurrent.setStrComment(strComment);
                    CellGroup.add(struCellCurrent);
                }

                //二级分组
                if (strColumnName.startsWith("「")&&strColumnName.endsWith("」")){
                    struCellCurrent.setIRow(row);
                    struCellCurrent.setICol(col);
                    if (strColumnName.length()==2){
                        struCellCurrent.setStrColumnName(strColumnName);
                    }else {
                        struCellCurrent.setStrColumnName(strColumnName.substring(1, strColumnName.length() - 1));
                    }
                    struCellCurrent.setStrComment(strComment);
                    Cell2Group.add(struCellCurrent);
                }
                }
            }
        }
    }
}
