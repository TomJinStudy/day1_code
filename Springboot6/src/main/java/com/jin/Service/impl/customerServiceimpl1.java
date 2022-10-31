package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Mapper.customerMapper;
import com.jin.Service.customerService;
import com.jin.domain.*;
import org.apache.commons.io.FileUtils;
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
    public void writeExcel(String finalXlsxPath,List<product> data,List<String> data1,List<product> group1,List<product> group2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
       Cell testCell=null;
       String[] customerfield = fieldName1(customer.class);
       String[] productfiled = fieldName1(product.class);
       Sheet sheet = workBook.getSheetAt(0);
       int startRow = getRownum(sheet, rowIndex,"<");
       int mergedRows = autoRowHeight.getMergedRows((XSSFRow) sheet.getRow(startRow));
       System.out.println(mergedRows);
       for (int i=mergedRows-1;i< mergedRows*(data.size()-1);i+=mergedRows){
           if(!template2.isRowEmpty(sheet.getRow(startRow+i+1))){
               sheet.shiftRows(startRow+i+1,sheet.getLastRowNum(),mergedRows);
               template2.copyRow(workBook,sheet.getRow(startRow),sheet.getRow(startRow+i+1),startRow+i+1);
           }else {
               template2.copyRow(workBook,sheet.getRow(startRow),sheet.getRow(startRow+i+1),startRow+i+1);
           }
       }
       for (int rownum=0;rownum<50;rownum++){
           Row row = sheet.getRow(rownum);
           if(row==null) continue;
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
                             cell.setCellValue("");
                             int colIndex = cell.getColumnIndex();
                             int rowNum = cell.getRow().getRowNum();
                             insertPicture(sheet,cell,data1.get(i), colIndex , rowNum,
                                     colIndex+getMergeColumNum(cell,sheet), rowNum+getMergeRowNum(cell,sheet));
                         }else {
                             cell.setCellValue(data1.get(i));}}}}
               if (s!=null&&s.contains("{")&&s.contains("}"))    testCell=cell;
           }
       }
        int colIndex1=0;
        int rowNum1=0;
        int chanshu=0;
       for (int rownum=startRow;rownum<50;rownum+=mergedRows,size++){
           Row row = sheet.getRow(rownum);
           if(row==null){
               continue;
           }
           for (int num=0;num<30;num++){
           Cell cell= row.getCell(num);
           if (cell==null)  continue;
           String s = formatter.formatCellValue(cell);
           if(s!=null&&s.contains("<")) {
               String substrings = formatter.formatCellValue(cell).substring(1, s.length() - 1);
               for (int i=0;i<productfiled.length;i++){
                   boolean equals = productfiled[i].equals(substrings);
                   if (equals){
                       if(substrings.equals("moq")) {
                           if (chanshu == 0) {
                               colIndex1 = cell.getColumnIndex();
                               rowNum1 = cell.getRow().getRowNum();
                               chanshu++;
                           }}
                       if (substrings.equals("pic")){
                         /*  cell.setCellValue("");
                           int colIndex = cell.getColumnIndex();
                           int rowNum = cell.getRow().getRowNum();
                           if(data.size()!=0)  insertPicture(sheet,cell, setValues(data.get(size),i), colIndex , rowNum , colIndex+1, rowNum+1);*/
                       }else {
                           if (data.size() == 0) cell.setCellValue("");
                           if (size < data.size()) cell.setCellValue(setValues(data.get(size), i));
                       }
                   }}}}
         autoRowHeight.cellAndSetRowHeigt(sheet,(XSSFRow) row);
       }
//如果要进行分组则执行以下代码
        if(getRownum(sheet,rowIndex,"(中文品名)")!=0){
            int rownum = getRownum(sheet, rowIndex, "(");
            int rownum1 = getRownum(sheet, rowIndex, "(销售数量)");
            List<Integer> notsame = notsame(sheet, rowNum1, colIndex1,mergedRows);
         //   List<Integer> integers = groupSum(data);
            System.out.println(notsame);
            List<Integer> inner1Sum = new ArrayList<>();
            List<Integer> sumPrice = new ArrayList<>();
            List<String> resort = new ArrayList<>();
            for (int i = 0; i < group1.size(); i++) {
                inner1Sum.add(group1.get(i).getInner1());
                sumPrice.add(group1.get(i).getOutsource());
                resort.add(group1.get(i).getMoq());
            }
            for (Iterator<Cell> cellIterator = sheet.getRow(rownum).cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                if (resort.size() > 0 && formatter.formatCellValue(cell).equals("(中文品名)")) {
                    cell.setCellValue(resort.get(0));
                }}
            int count = 0;
            for (int i = 0; i < notsame.size(); i++, count += 2) {
                sheet.shiftRows(notsame.get(i) + count, sheet.getLastRowNum(), 2);
                if (sheet.getRow(notsame.get(i) + count + 1) == null) sheet.createRow(notsame.get(i) + count + 1);
                if (sheet.getRow(notsame.get(i) + count + 2) == null) sheet.createRow(notsame.get(i) + count + 2);
                template2.copyRow(workBook, sheet.getRow(rownum1 + count + 2), sheet.getRow(notsame.get(i) + count), notsame.get(i) + count);
                template2.copyRow(workBook, sheet.getRow(rownum), sheet.getRow(notsame.get(i) + count + 1), notsame.get(i) + count + 1);
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count + 1).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s.equals("(中文品名)") | (resort.size()>0&&s.equals(resort.get(0)))) {
                        if (i + 1 < resort.size()) cell.setCellValue(resort.get(i + 1));
                    }
                }
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(i) + count).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (s.equals("(销售数量)")) cell.setCellValue(inner1Sum.get(i));
                    if (s.equals("(成交金额)")) cell.setCellValue(sumPrice.get(i));
                }
            }
            /*if (notsame.size() > 0) {
                for (Iterator<Cell> cellIterator = sheet.getRow(notsame.get(notsame.size() - 1) + count + integers.get(integers.size() - 1)).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    String s = formatter.formatCellValue(cell);
                    if (inner1Sum.size() > 0 && s.equals("(销售数量)"))
                        cell.setCellValue(inner1Sum.get(inner1Sum.size() - 1));// cell.setCellFormula("SUM("+s1+(rowIndex1-integers.get(integers.size()-1)+1)+":"+s1+rowIndex1+")");
                    if (sumPrice.size() > 0 && s.equals("(成交金额)"))
                        cell.setCellValue(sumPrice.get(sumPrice.size() - 1));//cell.setCellFormula("SUM("+s1+(rowIndex1-integers.get(integers.size()-1)+1)+":"+s1+rowIndex1+")");
                }
            } else {
                for (Iterator<Cell> cellIterator = sheet.getRow(rownum1).cellIterator(); cellIterator.hasNext(); ) {
                    Cell cell = cellIterator.next();
                    int rowIndex1 = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    String s = formatter.formatCellValue(cell);
                    String s1 = transString(columnIndex + 1);
                    if (integers.size() > 0 && s.equals("(销售数量)"))
                        cell.setCellFormula("SUM(" + s1 + (rowIndex1 - integers.get(integers.size() - 1)) + ":" + s1 + rowIndex1 + ")");
                    if (integers.size() > 0 && s.equals("(成交金额)"))
                        cell.setCellFormula("SUM(" + s1 + (rowIndex1 - integers.get(integers.size() - 1)) + ":" + s1 + rowIndex1 + ")");
                }
            }*/
            for (Iterator<Cell> cellIterator = sheet.getRow( getRownum(sheet,rowIndex,"(销售数量)")).cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                String s = formatter.formatCellValue(cell);
                if (inner1Sum.size() > 0 && s.equals("(销售数量)")) cell.setCellValue(inner1Sum.get(inner1Sum.size() - 1));// cell.setCellFormula("SUM("+s1+(rowIndex1-integers.get(integers.size()-1)+1)+":"+s1+rowIndex1+")");
                if (sumPrice.size() > 0 && s.equals("(成交金额)"))  cell.setCellValue(sumPrice.get(sumPrice.size() - 1));//cell.setCellFormula("SUM("+s1+(rowIndex1-integers.get(integers.size()-1)+1)+":"+s1+rowIndex1+")");
            }
        }
        mergeDown(sheet,testCell);
        restartinsert(startRow,mergedRows,sheet,formatter,productfiled,data);
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
   public  String setValues(product product,int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       String[] fieldNames = fieldName1(product.class);
           String methodName = "get" + fieldNames[i].substring(0,1).toUpperCase() + fieldNames[i].substring(1);//获取属性的get方法名
           Method method = product.getClass().getMethod(methodName);
       return method.invoke(product).toString();
   }
   //通过内容得到某个单元格的所在行
    public  static int getRownum(Sheet sheet,int Rownum ,String s1){
        DataFormatter formatter = new DataFormatter();
        for (int rownum=0;rownum<50;rownum++){
            Row row = sheet.getRow(rownum);
            if(row==null) continue;
            for (int cellNum=0;cellNum<30;cellNum++){
                Cell cell= row.getCell(cellNum);
                if (cell==null)  continue;
                String s = formatter.formatCellValue(cell);
                if (s!=null&&s.contains(s1)) {
                    if (Rownum==0) Rownum = cell.getRowIndex();
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
               value1 =formatter.formatCellValue(sheet.getRow(startRow + i+merginRows).getCell(col));}
              if(!value.equals(value1)){
                  if (value==null|value1==null|value1==""|value=="") break;
                  list.add(startRow+i+merginRows);}}
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
                   widths+=sheet1.getColumnWidthInPixels(cell.getColumnIndex()+i);}
                for (int i=0;i<getMergeRowNum(cell,sheet1);i++){
                    if(sheet1.getRow(cell.getRowIndex() +i)!=null){
                    heights+=sheet1.getRow(cell.getRowIndex() +i).getHeightInPoints();}}
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
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
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
    public  void restartinsert(int startRow,int mergedRows,Sheet sheet,DataFormatter formatter
    ,String[] productfiled,List<product> data) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int size=0;
        for (int rownum=startRow;rownum<50;rownum+=mergedRows){
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
                            if (substrings.equals("pic")){
                                cell.setCellValue("");
                                int colIndex = cell.getColumnIndex();
                                int rowNum = cell.getRow().getRowNum();
                                if(data.size()!=0&&size<data.size())  insertPicture(sheet,cell, setValues(data.get(size),i), colIndex , rowNum , colIndex+1, rowNum+1);
                                size++;
                            }
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
    //获得不重复的数据
   /* public static List<String> resort(List<product> la){
      Set<String> s = new TreeSet<>();
      ArrayList<String> objects = new ArrayList<>();
      for (int i=0;i<la.size();i++){
           s.add(la.get(i).getMoq());
        }
      for (Iterator<String> iterator = s.iterator();iterator.hasNext();) {
          objects.add( iterator.next());
      }
      return objects;
  }*/
  //统计分组数据中每组的行数放在列表中
   /* public static List<Integer> groupSum(List<product> sa){
      List<Integer> integers = new ArrayList<>();
      for (int i=0;i<sa.size();i++){
          int a=1;
          for (int j=i+1;j<sa.size();j++){
              if(sa.get(i).getMoq().equals(sa.get(j).getMoq())) {
                  a++;
              } else {i=j-1;
                  break;
              }
          }
         integers.add(a);
      }
      if (sa.size()>1&&sa.get(sa.size()-2).getMoq().equals(sa.get(sa.size()-1).getMoq())) integers.remove(integers.size()-1);
      return integers;
  }*/
  //判断向下合并单元格
    public static void mergeDown(Sheet sheet,Cell testcell){
      DataFormatter formatter = new DataFormatter();
      testcell.setCellValue("");
      int rowIndex1 = testcell.getRowIndex();
      int columnIndex = testcell.getColumnIndex();
      int i=1;
      while (sheet.getRow(rowIndex1+i)!=null&&(sheet.getRow(rowIndex1+i).getCell(columnIndex)==null
      |formatter.formatCellValue(sheet.getRow(rowIndex1+i).getCell(columnIndex))=="")){
          if (template2.isMergedRegion(sheet,rowIndex1,columnIndex)){
              int sheetMergeCount = sheet.getNumMergedRegions();
              int m=0;
              for (int n = 0; n< sheetMergeCount; n++) {
                  CellRangeAddress ca=sheet.getMergedRegion(n);
                  if(testcell.getRowIndex() >= ca.getFirstRow() && testcell.getRowIndex() <= ca.getLastRow()) {
                      if(testcell.getColumnIndex() >= ca.getFirstColumn() && testcell.getColumnIndex() <= ca.getLastColumn()) {
                          m=n;
                          sheet.removeMergedRegion(m);}}}}
          mergeRegin(sheet,rowIndex1,rowIndex1+i,columnIndex,columnIndex);
          i++;
      }
  }
  //将数字转换为对应的英文字母
   /* public static String transString(int a){
        int i = 'A' + a-1;
        Character i1 = (char) i;
        return i1.toString();
    }*/
}
