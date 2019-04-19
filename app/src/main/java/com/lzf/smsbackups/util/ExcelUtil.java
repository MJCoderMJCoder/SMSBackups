package com.lzf.smsbackups.util;

import android.content.Context;

import com.lzf.smsbackups.bean.SMSBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Excel 文件
 * <p>
 * Created by MJCoder on 2018-06-06.
 */

public class ExcelUtil {

    /**
     * @param context
     * @param title    Excel：sheet的头部标题
     * @param fileName Excel文件
     * @throws Exception
     */
    public static void writeExcel(Context context, String[] title, String fileName) throws Exception {
        File[] fileArray = FileUtil.mkdirsAndCackeFile(context, false, "SMSBackups", fileName);
        File file = new File(fileArray[0], fileName);
        if (!fileArray[1].exists()) {
            if (file.exists()) { //该文件已经存在；接着已经存在的部分内容继续添加数据或是断点续传补充数据
                appendExcelData(context, file, title);
            } else { //重新开始新建或是下载该文件
                createExcel(context, file, title);
                fileArray[1].createNewFile();
                FileOutputStream fOutputStream = new FileOutputStream(fileArray[1]);
                fOutputStream.write(("sizeMax：" + file.getName() + "；time：" + System.currentTimeMillis()).getBytes());
                fOutputStream.flush();
                fOutputStream.close();
            }
        } else {
            if (!file.exists()) { //重新开始新建或是下载该文件
                fileArray[1].delete();
                createExcel(context, file, title);
                fileArray[1].createNewFile();
                FileOutputStream fOutputStream = new FileOutputStream(fileArray[1]);
                fOutputStream.write(("sizeMax：" + file.getName() + "；time：" + System.currentTimeMillis()).getBytes());
                fOutputStream.flush();
                fOutputStream.close();
            } else { //该文件已经存在，且数据完整。
                appendExcelData(context, file, title);
            }
        }
    }

    /**
     * 创建Excel并写入初始化数据
     *
     * @param context
     * @param file    Excel文件
     * @param title   Excel：sheet的头部标题
     * @throws IOException
     * @throws WriteException
     */
    private static void createExcel(Context context, File file, String[] title) throws IOException, WriteException {
        List<SMSBean> smsBeans = SMSUtil.getAllSmsFromPhone(context);
        // 创建Excel工作表
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
        // 添加第一个工作表并设置第一个Sheet的名字
        WritableSheet sheet = writableWorkbook.createSheet(file.getName(), 0);
        for (int i = 0; i < title.length; i++) {
            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
            // 在Label对象的子对象中指明单元格的位置和内容
            // 将定义好的单元格添加到工作表中
            sheet.addCell(new Label(i, 0, title[i], getHeader()));
        }
        for (int i = 0; i < smsBeans.size(); i++) {
            SMSBean smsBean = smsBeans.get(i);
            sheet.addCell(new Label(0, i + 1, smsBean.getId()));
            sheet.addCell(new Label(1, i + 1, smsBean.getThread_id()));
            sheet.addCell(new Label(2, i + 1, smsBean.getAddress()));
            sheet.addCell(new Label(3, i + 1, smsBean.getPerson()));
            sheet.addCell(new Label(4, i + 1, smsBean.getDate()));
            sheet.addCell(new Label(5, i + 1, smsBean.getType()));
            sheet.addCell(new Label(6, i + 1, smsBean.getBody()));
        }
        writableWorkbook.write(); // 写入数据
        writableWorkbook.close(); // 关闭文件
    }

    /**
     * 给已有的Excel中添加（附加）数据
     *
     * @param context
     * @param file    Excel文件
     * @param title   Excel：sheet的头部标题
     * @throws IOException
     * @throws BiffException
     * @throws WriteException
     */
    private static void appendExcelData(Context context, File file, String[] title) throws IOException, BiffException, WriteException {
        List<SMSBean> smsBeans = SMSUtil.getSmsFromPhone(context);
        //创建只读的 Excel 工作薄的对象副本
        Workbook workbook = Workbook.getWorkbook(file);
        // 创建真实写入的 Excel 工作薄对象
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(file, workbook);
        //修改文本内容：例修改sheet2中cell B3的label内容
        WritableSheet sheet = writableWorkbook.getSheet(0);
        int rows = sheet.getRows();
        for (int i = 0; i < smsBeans.size(); i++) {
            SMSBean smsBean = smsBeans.get(i);
            sheet.addCell(new Label(0, i + rows, smsBean.getId()));
            sheet.addCell(new Label(1, i + rows, smsBean.getThread_id()));
            sheet.addCell(new Label(2, i + rows, smsBean.getAddress()));
            sheet.addCell(new Label(3, i + rows, smsBean.getPerson()));
            sheet.addCell(new Label(4, i + rows, smsBean.getDate()));
            sheet.addCell(new Label(5, i + rows, smsBean.getType()));
            sheet.addCell(new Label(6, i + rows, smsBean.getBody()));
        }
        //添加一个工作表
        //        WritableSheet addSheet = writableWorkbook.createSheet(file.getAbsolutePath(), writableWorkbook.getNumberOfSheets());
        //        for (int i = 0; i < title.length; i++) {
        //            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
        //            // 在Label对象的子对象中指明单元格的位置和内容
        //            // 将定义好的单元格添加到工作表中
        //            addSheet.addCell(new Label(i, 0, title[i], getHeader()));
        //        }
        writableWorkbook.write(); // 写入数据
        writableWorkbook.close(); // 关闭文件
    }

    /**
     * 设置 Excel：sheet的头部标题
     *
     * @return
     * @throws WriteException
     */
    private static WritableCellFormat getHeader() throws WriteException {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);// 定义字体
        font.setColour(Colour.BLUE);// 蓝色字体
        WritableCellFormat format = new WritableCellFormat(font);
        format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
        format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框
        format.setBackground(Colour.YELLOW);// 黄色背景
        return format;
    }
}
