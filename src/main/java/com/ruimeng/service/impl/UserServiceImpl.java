package com.ruimeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruimeng.entity.User;
import com.ruimeng.entity.WordLib;
import com.ruimeng.mapper.UserMapper;
import com.ruimeng.mapper.WordLibMapper;
import com.ruimeng.service.UserService;
import com.ruimeng.service.WordLibService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
