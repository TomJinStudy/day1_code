package com.jin.domain;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.util.HashMap;
import java.util.Map;

public class autoheight {
    public static void cellAndSetRowHeigt(XSSFRow sourceRow) {

        for (int cellIndex = sourceRow.getFirstCellNum(); cellIndex <= sourceRow.getPhysicalNumberOfCells(); cellIndex++) {

            double maxHeight = sourceRow.getHeight();
            if (cellIndex < 0) continue;
            XSSFCell sourceCell = sourceRow.getCell(cellIndex);
            String cellContent = getCellContentAsString(sourceCell);
            if (null == cellContent || "".equals(cellContent)) {
                continue;
            }
            //单元格的宽高及单元格信息
            Map<String, Object> cellInfoMap = getCellInfo(sourceCell);
            Integer cellWidth = (Integer) cellInfoMap.get("width");
            Integer cellHeight = (Integer) cellInfoMap.get("height");
            if (cellHeight > maxHeight) {
                maxHeight = cellHeight;
            }
            XSSFCellStyle cellStyle = sourceCell.getCellStyle();
            XSSFFont font = cellStyle.getFont();
            short fontHeight = font.getFontHeight();
            double cellContentWidth=0;
            if (fontHeight<=240){
                cellContentWidth = cellContent.getBytes().length * 235;
            } else if (fontHeight<=280) {
                cellContentWidth = cellContent.getBytes().length * 240;
            } else if (fontHeight<=320) {
                cellContentWidth = cellContent.getBytes().length * 265;
            }else if (fontHeight<=360){
                cellContentWidth = cellContent.getBytes().length * 350;
            }else if(fontHeight<=400){
                cellContentWidth = cellContent.getBytes().length * 500;
            }else {
                cellContentWidth = cellContent.getBytes().length * 2*256;
            }
            double stringNeedsRows = cellContentWidth / cellWidth;
            if (stringNeedsRows < 1.0) stringNeedsRows = 1.0;
            double stringNeedsHeight = (double) fontHeight * stringNeedsRows;

            if (stringNeedsHeight > maxHeight) {
                maxHeight = stringNeedsHeight;
               // if (maxHeight / cellHeight > 5) maxHeight = 4 * cellHeight;
                maxHeight = Math.ceil(maxHeight);
                Boolean isPartOfRowsRegion = (Boolean) cellInfoMap.get("isPartOfRowsRegion");
                if (isPartOfRowsRegion) {
                    Integer firstRow = (Integer) cellInfoMap.get("firstRow");
                    Integer lastRow = (Integer) cellInfoMap.get("lastRow");
                    double addHeight = (maxHeight - cellHeight) / (lastRow - firstRow + 1);
                    for (int i = firstRow; i <= lastRow; i++) {
                        double rowsRegionHeight = sourceRow.getSheet().getRow(i).getHeight() + addHeight;
                        sourceRow.getSheet().getRow(i).setHeight((short) rowsRegionHeight);
                    }
                } else {
                        sourceRow.setHeight((short) maxHeight);
                }
            }
        }
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
            if(lastRow > firstRow){ isPartOfRowsRegion = true;}
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
}
