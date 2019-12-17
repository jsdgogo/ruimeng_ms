package com.ruimeng.service;

import com.ruimeng.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
public interface OrderService extends IService<Order> {

    boolean deleteById(int id);

    boolean add(Order order);
}
