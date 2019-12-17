package com.ruimeng.vo;

import lombok.Getter;

/**
 * @author JiangShiDing
 * @description 统一返回数据格式
 * @Date 2019/11/27
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 0,"成功"),
    UNKNOWN_REASON(false, 1, "未知错误"),
    BAD_SQL_GRAMMAR(false, 10001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 10002, "json解析异常"),
    PARAM_ERROR(false, 10003, "参数不正确"),
    FILE_UPLOAD_ERROR(false, 10004, "文件上传错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 10005, "Excel数据导入错误");

    private Boolean success;

    private Integer code;

    private String message;

    private ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}