package com.ruimeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JiangShiDing
 * @description 用户实体
 * @Date 2019/12/9 0009
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int LEVEL_ADMIN = 1;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name; //用户名
    @TableField("loginName")
    private String loginName; //登录名
    private String password; //密码
    private int level;//级别
    @TableField("createTime")
    private Date createTime; //创建时间
    @TableField("updateTime")
    private Date updateTime; //修改时间
    private String token; //令牌
}
