package com.ruimeng.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p>
 * 
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 * @description 订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("customerId")
    private int customerId; //客户id

    @TableField("customerName")
    private String customerName; //客户名
    private int quantity; //购买数量

    @TableField("totalPrice")
    private double totalPrice; //订单总价

    private double paid; //客户付款金额
    @TableField("createTime")
    private Date createTime; //创建时间

    @TableField("updateTime")
    private Date updateTime; //修改时间

}
