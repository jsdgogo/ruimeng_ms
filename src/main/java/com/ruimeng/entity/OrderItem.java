package com.ruimeng.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 * @description 订单商品实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("orderId")
    private int orderId; //订单id

    private int quantity; //购买数量

    private double price; //气瓶单价
    @TableField("gasCylinderId")
    private int gasCylinderId;//气瓶id
    @TableField("gasCylinderName")
    private String gasCylinderName; //气瓶类型
}
