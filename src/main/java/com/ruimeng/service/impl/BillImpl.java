package com.ruimeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruimeng.entity.Bill;
import com.ruimeng.mapper.BillMapper;
import com.ruimeng.service.BillService;
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
public class BillImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

}
