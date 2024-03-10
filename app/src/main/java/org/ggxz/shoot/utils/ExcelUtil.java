package org.ggxz.shoot.utils;

import android.content.Context;
import android.widget.Toast;

import org.ggxz.shoot.bean.UserHistoryBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 3、创建工具类：ExcelUtil, 注释非常详细就不解释了
 */
public class ExcelUtil {
    private static WritableFont arial14font = null;//可写字体
    private static WritableCellFormat arial14format = null;//单元格格式
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    //单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
    private static void format() {
        try {
            //字体 ARIAL， 字号 14  bold  粗体
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);//字体的颜色
            arial14font.setUnderlineStyle(UnderlineStyle.SINGLE);//设置下划线


            //初始化单元格格式
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(Alignment.CENTRE);//对齐方式
            arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);//边框的格式
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);//底色

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(Alignment.CENTRE);
            arial10format.setBorder(Border.ALL, BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setAlignment(Alignment.CENTRE);
            arial12format.setBorder(Border.ALL, BorderLineStyle.THIN);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     * 写入字段名称，表名
     *
     * @param filePath  导出excel的存放地址
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        //创建一个工作薄，就是整个Excel文档
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            //使用Workbook创建一个工作薄，就是整个Excel文档
            workbook = Workbook.createWorkbook(file);
            //设置表格的名称(两个参数分别是工作表名字和插入位置，这个位置从0开始)
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建label标签：实际就是单元格的标签（三个参数分别是：col + 1列，row + 1行， 内容， 单元格格式）
//            Label label = new Label(0, 0, filePath, arial14format);//设置第一行的单元格标签为：标题
            //将标签加入到工作表中
//            sheet.addCell(label);

            //通过writablesheet.mergeCells(int x,int y,int m,int n);来实现的。
            // 表示将从第x+1列，y+1行到m+1列，n+1行合并 (四个点定义了两个坐标，左上角和右下角)
            sheet.mergeCells(0, 0, colName.length - 1, 0);
            sheet.addCell(new Label(0, 0, "射击成绩统计报表", arial14format));
            sheet.setRowView(0, 520);

            //再同一个单元格中写入数据，上一个数据会被下一个数据覆盖
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 1, colName[col], arial10format));
            }
            //设置行高 参数的意义为（第几行， 行高）
            sheet.setRowView(1, 340);
            workbook.write();// 写入数据
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();// 关闭文件
                } catch (IOException | WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 见指定类型的List写入到Excel文件中
     *
     * @param objList  代写入的List
     * @param fileName
     * @param context
     * @param <T>
     */
    public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context context) {
        if (objList != null && objList.size() > 0) {
            //创建一个工作薄，就是整个Excel文档
            WritableWorkbook writeBook = null;
            InputStream in = null;

            try {
                WorkbookSettings settings = new WorkbookSettings();
                settings.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                //Workbook不但能用来创建工作薄，也可以读取现有的工作薄
                Workbook workbook = Workbook.getWorkbook(in);
                //创建一个工作薄，就是整个Excel文档
                writeBook = Workbook.createWorkbook(new File(fileName), workbook);
                //读取表格
                WritableSheet sheet = writeBook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    UserHistoryBean demoBean = (UserHistoryBean) objList.get(j);
                    List<String> list = new ArrayList<>();
                    list.add(demoBean.getCreateTime());
                    list.add(String.valueOf(demoBean.getUserName()));
                    list.add(String.valueOf(demoBean.getTotalGrade()));
                    list.add(String.valueOf(demoBean.getTotalCollimation()));
                    list.add(String.valueOf(demoBean.getTotalShoot()));
                    list.add(String.valueOf(demoBean.getTotalAll()));

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 2, list.get(i), arial12format));//向一行中添加数据
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }
                writeBook.write();
                workbook.close();
                Toast.makeText(context, "导出Excel成功,文件地址:"+fileName, Toast.LENGTH_SHORT).show();
            } catch (IOException | BiffException | WriteException e) {
                e.printStackTrace();
            } finally {
                if (writeBook != null) {
                    try {
                        writeBook.close();
                    } catch (IOException | WriteException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
