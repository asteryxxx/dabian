package net.sppan.base.utils;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class exportUtils {
    public static void createExcel(List list, List<String> lietitles,List<String> ziduanes, Integer lie, String shouhangbiaoti) throws Exception {

        //创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建一个sheet
        XSSFSheet sheet = wb.createSheet();
        for(int i=0;i<lie;i++){
            if(i==lie-1){
                sheet.setColumnWidth(lie-1, 30 * 256);
            }
            if(i!=0){
                sheet.setColumnWidth(i, 20 * 256);
            }
        }

        XSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        // 创建单元格样式
        XSSFCellStyle style =  wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex()); //设置要添加表格北京颜色
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //文字水平居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//文字垂直居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN); //底边框加黑
        style.setBorderLeft(BorderStyle.THIN);  //左边框加黑
        style.setBorderRight(BorderStyle.THIN); // 有边框加黑
        style.setBorderTop(BorderStyle.THIN); //上边框加黑
        style.setFont(font);
        //为单元格添加背景样式

        for (int i = 0; i <list.size()+2 ; i++) { //需要6行表格
            Row row=null;
            if(i==0||i==1){
                row =sheet.createRow(i); //创建行
                row.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
            }
            row =sheet.createRow(i); //创建行
            for (int j = 0; j < lie; j++) {//需要6列
                row.createCell(j).setCellStyle(style);
            }
        }


        //合并单元格 //参数1：行号 参数2：起始列号 参数3：行号 参数4：终止列号
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lie-1));

        //tian入数据
        XSSFRow row = sheet.getRow(0); //获取第一行
        row.getCell(0).setCellValue(shouhangbiaoti); //在第一行中创建一个单元格并赋值
        row.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());

        XSSFRow row1 = sheet.getRow(1); //获取第二行，为每一列添加字段
        for(int i=0;i<lietitles.size();i++) {
            row1.getCell(i).setCellValue(lietitles.get(i));
        }
        row1.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());

        XSSFCellStyle neirongStyle = wb.createCellStyle();
        // 竖向居中
        neirongStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); //文字水平居中
        neirongStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//文字垂直居中
        neirongStyle.setAlignment(HorizontalAlignment.CENTER);
        neirongStyle.setBorderBottom(BorderStyle.THIN); //底边框加黑
        neirongStyle.setBorderLeft(BorderStyle.THIN);  //左边框加黑
        neirongStyle.setBorderRight(BorderStyle.THIN); // 有边框加黑
        neirongStyle.setBorderTop(BorderStyle.THIN); //上边框加黑
        Date date = new Date();
        //设置要获取到什么样的时间
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间

        int rowIndex = 2 ;
        for(int j=0;j<list.size();j++) {
            Row row666=sheet.createRow(rowIndex);
            row666.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
                Cell r = row666.createCell(0);
                r.setCellStyle(neirongStyle);
                r.setCellValue(j+1);
            for(int i=0;i<ziduanes.size();i++) {
                //反射根据对象的属性获取到值
                Cell r1 = row666.createCell(i+1);
                r1.setCellStyle(neirongStyle);
                Object t = list.get(j);
                String zhi = getValueByPropName(t, ziduanes.get(i));
                if(zhi.equals("")){
                    r1.setCellValue("");
                }
                r1.setCellValue(zhi);
            }
            rowIndex++;
        }
        //将数据写入文件
        String fileName=new String(("部门信息 "+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"UTF-8");
          FileOutputStream out = new FileOutputStream(new File("d://"+fileName+".xlsx"));
        wb.write(out);
        out.flush();
        out.close();
    }

    // 通过属性获取传入对象的指定属性的值
    public static String getValueByPropName(Object t, String propName)  {
        String value = null;
        try {
            // 通过属性获取对象的属性
            Field field = t.getClass().getDeclaredField(propName);

            //判断属性类型是不是时间类型是就转成字符串

            // 对象的属性的访问权限设置为可访问
            field.setAccessible(true);
            // 获取属性的对应的值
            Object tempvalue = field.get(t);

            if (field.getGenericType().toString().equals(
                    "class java.util.Date")){
                System.out.println("是时间类型的---");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date da = (Date) tempvalue;
                // 对象的属性的访问权限设置为可访问
                String sj = sdf.format(da);
                value=sj;
            }else {
                value = tempvalue.toString();
            }
            System.out.println("值="+value);
        } catch (Exception e) {
            return null;
        }

        return value;
    }
}
