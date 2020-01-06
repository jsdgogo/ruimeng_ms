package com.ruimeng.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private int id;
    private int gasCylinderId;
    private int quantity;
    private double price;
    private int inventory;
    private String name;
}
