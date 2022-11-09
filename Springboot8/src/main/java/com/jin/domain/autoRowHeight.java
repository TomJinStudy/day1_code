package com.jin.domain;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class autoRowHeight {
    public static void cellAndSetRowHeigt(Sheet sheet, XSSFRow sourceRow) {
        int count=1;
        int maxColumnIndex=30;
        double MaxRows=0;
        if (sourceRow==null) return;
        for (int cellIndex = sourceRow.getFirstCellNum(); cellIndex <= sourceRow.getPhysicalNumberOfCells(); cellIndex++,count++) {
            if (cellIndex < 0) continue;
            XSSFCell sourceCell = sourceRow.getCell(cellIndex);
            String cellContent = getCellContentAsString(sourceCell);
            if (null == cellContent || "".equals(cellContent)) {
                continue;
            }
            Map<String, Object> cellInfoMap = getCellInfo(sourceCell);
            Integer cellWidth = (Integer) cellInfoMap.get("width");
            double cellContentWidth = 0;
            try {
                cellContentWidth = sourceCell.toString().getBytes("GBK").length * 256;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            // double cellContentWidth = cellContent.length() * 256;
            double stringNeedsRows = cellContentWidth / cellWidth;
            if (stringNeedsRows>MaxRows)  MaxRows=stringNeedsRows;
            if(getMergeColumNum(sourceCell,sheet)!=1){
                sheet.setColumnWidth(maxColumnIndex+count,cellWidth);
                XSSFCell cell = sourceRow.createCell(maxColumnIndex+ count);
                CellStyle newStyle = sheet.getWorkbook().createCellStyle();
                template2.copyCellStyle(sourceCell.getCellStyle(),newStyle, sheet.getWorkbook());
                cell.setCellStyle(newStyle);
                //cell.getCellStyle().setFont(sourceCell.getCellStyle().getFont());
                cell.setCellValue(getCellContentAsString(sourceCell));
            }
        }
        for (int i=30;i<60;i++){
            sheet.setColumnHidden(i,true);
        }
        if (MaxRows<1) {
           return;
        }
        System.out.println("启动了自动行高"+MaxRows);
        sourceRow.getCTRow().setCustomHeight(false);
    }
    public static int getMergedRows(XSSFRow sourceRow){
        int Rows=1;
        int MaxRow=1;
        for (int cellIndex = sourceRow.getFirstCellNum(); cellIndex <= sourceRow.getPhysicalNumberOfCells(); cellIndex++) {
            Integer firstRow=0;
            Integer lastRow=0;
            if (cellIndex<0) continue;
            XSSFCell sourceCell =sourceRow.getCell(cellIndex);
            if (sourceCell!=null){
                Map<String, Object> cellInfoMap = getCellInfo(sourceCell);
                Boolean isPartOfRowsRegion = (Boolean)cellInfoMap.get("isPartOfRowsRegion");
                if(isPartOfRowsRegion){
                    firstRow = (Integer)cellInfoMap.get("firstRow");
                    lastRow =  (Integer)cellInfoMap.get("lastRow");
                }
                Rows=lastRow-firstRow+1;
                if (Rows>MaxRow) MaxRow=Rows;
            }
        }
        return MaxRow;
    }
    private static String getCellContentAsString(XSSFCell cell) {
        if(null == cell){ return "";}
        String result ="";
        switch (cell.getCellType()) {
            case NUMERIC:
                String s = String.valueOf(cell.getNumericCellValue());
                if (s != null) {
                    if (s.endsWith(".0")) {
                        s = s.substring(0, s.length() - 2);}}
                result = s;
                break;
            case STRING:
                result = String.valueOf(cell.getRichStringCellValue());
                break;
            case BLANK:
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                break;
            default:
                break;
        }
        return result;
    }

    public static Map<String,Object> getCellInfo(XSSFCell cell) {
        Map<String,Object> map = new HashMap();
        XSSFSheet sheet = cell.getSheet();
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        boolean isPartOfRegion = false;
        int firstColumn = 0;
        int lastColumn = 0;
        int firstRow = 0;
        int lastRow = 0;
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress c = sheet.getMergedRegion(i);
            firstColumn = c.getFirstColumn();
            lastColumn = c.getLastColumn();
            firstRow = c.getFirstRow();
            lastRow = c.getLastRow();
            if (rowIndex >= firstRow && rowIndex <= lastRow) {
                if (columnIndex >= firstColumn && columnIndex <= lastColumn) {
                    isPartOfRegion = true;
                    break;
                }
            }
        }
        Integer width = 0;
        Integer height = 0;
        boolean isPartOfRowsRegion = false;
        if(isPartOfRegion){
            for (int i = firstColumn; i <= lastColumn; i++) {
                width += sheet.getColumnWidth(i);
            }
            for (int i = firstRow; i <= lastRow; i++) {
                if (sheet.getRow(i)==null){
                    height+=sheet.createRow(i).getHeight();
                }else {
                    height += sheet.getRow(i).getHeight();
                }
            }
            if(lastRow > firstRow) isPartOfRowsRegion = true;
        }else{
            width = sheet.getColumnWidth(columnIndex);
            height += cell.getRow().getHeight();
        }
        map.put("isPartOfRowsRegion", isPartOfRowsRegion);
        map.put("firstRow", firstRow);
        map.put("lastRow", lastRow);
        map.put("width", width);
        map.put("height", height);
        return map;
    }
    public static int getMergeColumNum(Cell cell, Sheet sheet) {
        int mergeSize = 1;
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress cellRangeAddress : mergedRegions) {
            if (cellRangeAddress.isInRange(cell)) {
                mergeSize = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
                break;
            }
        }
        return mergeSize;
    }
}
