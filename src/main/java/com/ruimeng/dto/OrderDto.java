package com.ruimeng.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private int id;
    private CustomerDto customer;
    private List<OrderItemDto> orderItems;
    private String createTimeStr;
    private double paid;
    private double total;
    private int number;
    private String updateTime;
    private double orderDebt; //订单欠款
}
