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
public class CustomerEmptyBottle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("emptyBottleId")
    private Integer emptyBottleId; //空瓶id

    private String total; //所欠空瓶总数量

    @TableField("customerId")
    private Integer customerId; //客户id

    @TableField("customerName")
    private String customerName;  //客户名

    @TableField("sendBackNumber")
    private Integer sendBackNumber;  //已归还数量

    @TableField("createTime")
    private Date createTime;  //创建时间

    @TableField("updateTime")
    private Date updateTime; //修改时间


}
