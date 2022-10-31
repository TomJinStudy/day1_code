package com.jin.domain;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;

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
          //  toStyle.setWrapText(true);
        }
       public static void copyRow(Workbook wb,Row fromRow,Row toRow,int i){
           Sheet sheet = wb.getSheetAt(0);
           if (fromRow!=null){
               int count=0;
               Row row=null;
        //   for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
               for (int n=0;n<fromRow.getLastCellNum();n++) {
                   Cell tmpCell = fromRow.getCell(n);
                   if (tmpCell==null){
                       tmpCell = fromRow.createCell(n);}
               if (toRow==null) {
                   if(row==null) row = sheet.createRow(i);
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
                   count++;}
           }}
       }
    public static void mergerRegion(Sheet Sheet,Row row,Row fromrow) {
        CellRangeAddress region = null;
        for (int i = 0; i < Sheet.getNumMergedRegions(); i++) {
            region = Sheet.getMergedRegion(i);
            if (region.getFirstRow()==fromrow.getRowNum()){
                CellRangeAddress address = new CellRangeAddress(row.getRowNum(), (row.getRowNum() + region.getLastRow() - region.getFirstRow()),
                        region.getFirstColumn(), region.getLastColumn());
                if (!isMergedRegion(Sheet,row.getRowNum(),region.getFirstColumn())){
                    Sheet.addMergedRegion(address);
                }
            }
        }
    }
    public static  boolean isRowEmpty(Row row){
        if(row==null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
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
        public static void copyCell(Workbook wb, Cell srcCell, Cell distCell) {
            CellStyle newstyle=wb.createCellStyle();
            copyCellStyle(srcCell.getCellStyle(), newstyle,wb);
            distCell.setCellStyle(newstyle);
            if (srcCell.getCellComment() != null) {
                distCell.setCellComment(srcCell.getCellComment());}
                CellType srcCellType = srcCell.getCellType();
                if (srcCellType == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(srcCell)) {distCell.setCellValue(srcCell.getDateCellValue());}
                    else {distCell.setCellValue(srcCell.getNumericCellValue());}
                } else if (srcCellType == CellType.STRING) {
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                } else if (srcCellType == CellType.BLANK) {
                    //distCell.setCellValue("");
                } else if (srcCellType == CellType.BOOLEAN) {
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                } else if (srcCellType == CellType.ERROR) {
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                } else if (srcCellType == CellType.FORMULA) {
                    String cellFormula=srcCell.getCellFormula();
                    distCell.setCellFormula(cellFormula.replace(String.valueOf(srcCell.getRowIndex()+1), String.valueOf(distCell.getRow().getRowNum()+1)));
                } else {
                   distCell.setCellValue(srcCell.getStringCellValue());
                }
        }

    }


