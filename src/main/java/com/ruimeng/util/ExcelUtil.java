package com.ruimeng.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 操作Excel表格的功能类
 */
public class ExcelUtil {

    /**
     * @Title: createExecl
     * @Description: 创建execl
     * @param titles 标题行
     * @param alias 对应字段
     * @param list 数据
     * @return Workbook 返回类型
     */
    public static Workbook createExecl(String[] titles, String[] alias, List list) {
        JSONArray ja = JSONArray.parseArray(JSON.toJSONString(list));
        return createExecl(titles, alias, ja);
    }

    /**
     * @Title: createExecl
     * @Description: 创建execl
     * @param titles 标题行
     * @param alias 对应字段
     * @param data 数据
     * @return Workbook 返回类型
     */
    public static Workbook createExecl(String[] titles, String[] alias, JSONArray data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles[i]);
        }
        Iterator<Object> it = data.iterator();
        int j = 1;
        while (it.hasNext()) {
            JSONObject jsonObject = (JSONObject) it.next();
            Row tmpRow = sheet.createRow(j);
            for (int i = 0; i < alias.length; i++) {
                Cell cell = tmpRow.createCell(i);
                Object param = jsonObject.get(alias[i]);
                if (param instanceof Integer) {
                    cell.setCellValue(((Integer) param).intValue());
                } else if (param instanceof String) {
                    cell.setCellValue((String) param);
                } else if (param instanceof Double) {
                    double d = ((Double) param).doubleValue();
                    cell.setCellValue(d);
                } else if (param instanceof Float) {
                    float f = ((Float) param).floatValue();
                    cell.setCellValue(f);
                } else if (param instanceof Long) {
                    long l = ((Long) param).longValue();
                    cell.setCellValue(new Date(l));
                    CellStyle cellStyle = workbook.createCellStyle();
                    DataFormat dateFormat = workbook.createDataFormat();
                    cellStyle.setDataFormat(dateFormat.getFormat("yyyy/MM/dd HH:mm:ss"));
                    cell.setCellStyle(cellStyle);
                } else if (param instanceof Boolean) {
                    cell.setCellValue(((Boolean) param).booleanValue());
                } else if (param instanceof Date) {
                    cell.setCellValue((Date) param);
                } else if (param instanceof BigDecimal) {
                    BigDecimal d = (BigDecimal) param;
                    cell.setCellValue(d.setScale(2, RoundingMode.HALF_UP).doubleValue());
                    CellStyle cellStyle = workbook.createCellStyle();
                    cell.setCellStyle(cellStyle);
                }
            }
            j++;
        }
        return workbook;
    }

}
