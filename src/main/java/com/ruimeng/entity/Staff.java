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
 * @description 员工实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String name; //员工名

    private int age;//员工年龄

    private String phone; //联系电话

    @TableField("workYears")
    private String workYears; //工龄

    private double salary; //月工资

    private String position; //职位

    @TableField("createTime")
    private Date createTime; //创建时间

    @TableField("updateTime")
    private Date updateTime; //修改时间


}
