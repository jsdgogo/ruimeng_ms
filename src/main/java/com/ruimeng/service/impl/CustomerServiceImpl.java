package com.ruimeng.service.impl;

import com.ruimeng.entity.Customer;
import com.ruimeng.mapper.CustomerMapper;
import com.ruimeng.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
