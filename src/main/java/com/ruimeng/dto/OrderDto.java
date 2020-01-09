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
}
