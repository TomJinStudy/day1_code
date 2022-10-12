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
            toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
            toStyle.setFillForegroundColor((short)250);
            toStyle.setDataFormat(fromStyle.getDataFormat());
            FillPatternType fillPattern = fromStyle.getFillPattern();
            toStyle.setFillPattern(fillPattern);
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
               if (toRow==null) {
                   if(row==null) {
                       row = sheet.createRow(i);
                   }
                   mergerRegion(sheet,row,fromRow);
                   row.setHeightInPoints(fromRow.getHeightInPoints());
                   Cell newCell = row.createCell(count);
                   copyCell(wb,tmpCell, newCell);
                   count++;
               }else {
                   mergerRegion(sheet,toRow,fromRow);
               toRow.setHeightInPoints(fromRow.getHeightInPoints());
                   Cell newCell = toRow.createCell(count);
                   copyCell(wb,tmpCell, newCell);
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
                Sheet.addMergedRegionUnsafe(address);
            }
        }
    }
        public static void copyCell(Workbook wb, Cell srcCell, Cell distCell) {
            CellStyle newstyle=wb.createCellStyle();
            copyCellStyle(srcCell.getCellStyle(), newstyle,wb);
            distCell.setCellStyle(newstyle);
            if (srcCell.getCellComment() != null) {
                distCell.setCellComment(srcCell.getCellComment());
            }
            CellType srcCellType = srcCell.getCellType();
                if (srcCellType == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(srcCell)) {
                        distCell.setCellValue(srcCell.getDateCellValue());
                    } else {
                        distCell.setCellValue(srcCell.getNumericCellValue());
                    }
                } else if (srcCellType == CellType.STRING) {
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                } else if (srcCellType == CellType.BLANK) {
                } else if (srcCellType == CellType.BOOLEAN) {
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                } else if (srcCellType == CellType.ERROR) {
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                } else if (srcCellType == CellType.FORMULA) {
                    distCell.setCellFormula(srcCell.getCellFormula());
                } else {
                }

        }
    }


