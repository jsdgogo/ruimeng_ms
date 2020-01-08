package com.ruimeng.dto;


import lombok.Data;

import java.util.Date;

@Data
public class OrderExcelDto {
    private int id;
    private int customerId; //客户id
    private String customerName; //客户名
    private int total; //购买数量
    private double totalPrice; //订单总价
    private Date createTime; //创建时间
    private int quantity; //购买数量
    private double price; //气瓶单价
    private int gasCylinderId;//气瓶id
    private String gasCylinderName; //气瓶类型

}
