package com.ruimeng.vo;

import lombok.Data;

/**
 * @author JiangShiDing
 * @description 描述
 * @Date 2019/12/3 0003
 */
@Data
public class PageParam {
    private static boolean ASC = true;//升序
    private static boolean DESC = false;//降序
    private int index = 1; //页码
    private int size = 8;//页长
    private String orderBy;//排序字段
    private boolean ascOrDesc = DESC;//降序或者升序
    private String search; //搜索字段
}
