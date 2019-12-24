package com.ruimeng.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private int gasCylinderId;
    private int quantity;
    private double price;
}
