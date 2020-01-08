package com.ruimeng.entity;

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
 * @description 客户空瓶实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @TableField("customerId")
    private int customerId; //客户id
    @TableField("customerName")
    private String customerName;  //客户名
    @TableField("totalDebt")
    private double totalDebt; //总欠款
    @TableField("orderDebt")
    private double orderDebt; //订单欠款
    @TableField("orderTotal")
    private double orderTotal; //订单总金额
    private double paid; //已付款
    @TableField("emptyBottleTotal")
    private double emptyBottleTotal;
    @TableField("createTime")
    private Date createTime;  //创建时间
    @TableField("updateTime")
    private Date updateTime; //修改时间


}
