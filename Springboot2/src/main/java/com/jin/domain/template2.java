package com.jin.domain;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.Iterator;

public class template2 {

        public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle,Workbook wb) {

            toStyle.setAlignment(fromStyle.getAlignment());
            toStyle.setBorderBottom(fromStyle.getBorderBottom());
            toStyle.setBorderLeft(fromStyle.getBorderLeft());
            toStyle.setBorderRight(fromStyle.getBorderRight());
            toStyle.setBorderTop(fromStyle.getBorderTop());
            toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
            toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
            toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
            toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
            toStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
            toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
            toStyle.setDataFormat(fromStyle.getDataFormat());
            toStyle.setFillPattern(FillPatternType.NO_FILL);
            toStyle.setFont(wb.getFontAt(fromStyle.getFontIndex()));
            toStyle.setHidden(fromStyle.getHidden());
            toStyle.setIndention(fromStyle.getIndention());
            toStyle.setLocked(fromStyle.getLocked());
            toStyle.setRotation(fromStyle.getRotation());
            toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
            toStyle.setWrapText(fromStyle.getWrapText());
        }
       public static void copyRow(Workbook wb,Row fromRow,Row toRow,int i){
           Sheet sheet = wb.getSheetAt(0);
           if (fromRow!=null){
               int count=0;
               Row row=null;
           for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
               XSSFCell tmpCell = (XSSFCell) cellIt.next();
              /*if (tmpCell.getCellType()==CellType.FORMULA) tmpCell.removeFormula();
              if(tmpCell.getColumnIndex()==0) tmpCell.removeFormula();
               int rowNum1 = toRow.getRowNum();*/
               if (toRow==null) {
                   if(row==null) row = sheet.createRow(i);
                   int rowNum1 = row.getRowNum();
                   mergerRegion(sheet,row,fromRow);
                   row.setHeightInPoints(fromRow.getHeightInPoints());
                   Cell newCell = row.createCell(count);
                   copyCell(wb,tmpCell, newCell,sheet);
                   if(tmpCell.getColumnIndex()==0) newCell.setCellFormula("ROW(B"+(rowNum1+1)+")-12");
                   count++;
               }else {
                   int rowNum = toRow.getRowNum();
                   mergerRegion(sheet,toRow,fromRow);
               toRow.setHeightInPoints(fromRow.getHeightInPoints());
                   Cell newCell = toRow.createCell(count);
                   copyCell(wb,tmpCell, newCell,sheet);
                   if(tmpCell.getColumnIndex()==0) newCell.setCellFormula("ROW(B"+(rowNum+1)+")-12");
                   count++;
               }
           }}
       }

    public static void mergerRegion(Sheet Sheet,Row row,Row fromrow) {
        CellRangeAddress region = null;
        for (int i = 0; i < Sheet.getNumMergedRegions(); i++) {
            region = Sheet.getMergedRegion(i);
            if (region.getFirstRow()==fromrow.getRowNum()){
                CellRangeAddress address = new CellRangeAddress(row.getRowNum(), row.getRowNum(),
                        region.getFirstColumn(), region.getLastColumn());
                if (!isMergedRegion(Sheet,row.getRowNum(),region.getFirstColumn())){
                    Sheet.addMergedRegion(address);
                }
            }
        }
    }
    public static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca=sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if(row >= firstRow && row <= lastRow) {
                if(column >= firstColumn && column <= lastColumn) {
                    return true;}
            }}
        return false;}
        public static void copyCell(Workbook wb, Cell srcCell, Cell distCell,Sheet sheet) {
            CellStyle newstyle=wb.createCellStyle();
            copyCellStyle(srcCell.getCellStyle(), newstyle,wb);
            distCell.setCellStyle(newstyle);
            if (srcCell.getCellComment() != null) {
                distCell.setCellComment(srcCell.getCellComment());
            }
            CellType srcCellType = srcCell.getCellType();
                if (srcCellType == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(srcCell)) {
                        distCell.setCellValue(srcCell.getDateCellValue());}
                    else {
                        distCell.setCellValue(srcCell.getNumericCellValue());}
                } else if (srcCellType == CellType.STRING) {
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                } else if (srcCellType == CellType.BLANK) {
                } else if (srcCellType == CellType.BOOLEAN) {
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                } else if (srcCellType == CellType.ERROR) {
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                } else if (srcCellType == CellType.FORMULA) {
                 // System.out.println(srcCell.getCellFormula());
                   String s=srcCell.getCellFormula();
                    distCell.setCellFormula(srcCell.getCellFormula());
                    //  distCell.setCellFormula("ROW(B3)");


                } else {
                   distCell.setCellValue(srcCell.getStringCellValue());

                }

        }
    }


