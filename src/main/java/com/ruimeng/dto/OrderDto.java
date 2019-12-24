package com.ruimeng.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private CustomerDto customer;
    private List<OrderItemDto> orderItems;
    private String createTimeStr;
}
