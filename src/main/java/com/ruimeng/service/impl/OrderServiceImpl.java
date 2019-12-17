package com.ruimeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.Order;
import com.ruimeng.entity.OrderItem;
import com.ruimeng.mapper.OrderItemMapper;
import com.ruimeng.mapper.OrderMapper;
import com.ruimeng.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;


    @Override
    public boolean deleteById(int id) {
        int deleteOrder = orderMapper.deleteById(id);
        if (deleteOrder == 1) {
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("orderId", id);
            orderItemMapper.delete(queryWrapper);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Order order) {
        Date createTime = new Date();
        order.setCreateTime(createTime);
        order.setUpdateTime(createTime);
        int insert = orderMapper.insert(order);
        if (insert != 0) {
            Integer orderId = order.getId();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.setCreateTime(createTime);
                orderItem.setUpdateTime(createTime);
                orderItem.setOrderId(orderId);
                orderItemMapper.insert(orderItem);
            }
            return true;
        }
        return false;
    }
}
