package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.UserOrder;
import org.example.mymallapplication.dal.dao.mapper.product.UserOrderMapper;
import org.example.mymallapplication.dal.dao.service.product.IUserOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements IUserOrderService {

}
