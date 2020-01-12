package com.ruimeng.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruimeng.entity.CustomerEmptyBottle;
import com.ruimeng.entity.Orders;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author JiangShiDing
 * @description 描述
 * @Date 2020/1/11 0011
 */
@Data
public class BillDto {
    private int id;
    private String customerName;  //客户名
    private double totalDebt; //总欠款
    private double orderDebt; //订单欠款
    private double orderTotal; //订单总金额
    private double paid; //已付款
    private double emptyBottleTotal;
    private Date createTime;  //创建时间
    private List<OrderDto> orders;
    private List<CustomerEmptyBottle> customerEmptyBottles;
}
