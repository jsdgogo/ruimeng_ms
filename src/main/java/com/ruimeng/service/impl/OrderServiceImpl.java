package com.ruimeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruimeng.entity.Orders;
import com.ruimeng.mapper.OrderMapper;
import com.ruimeng.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

}
