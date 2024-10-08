package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.UserOrder;
import org.example.mymallapplication.dal.dao.mapper.product.UserOrderMapper;
import org.example.mymallapplication.dal.dao.service.product.IUserOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * <p>获取订单ID列表</p>
     *
     * @param userId 用户ID
     * @return 订单ID列表
     */
    @Override
    public List<String> getOrderId(String userId) {
        LambdaQueryWrapper<UserOrder> queryWrapper = new LambdaQueryWrapper<>();

        return this.list(queryWrapper).stream()
                .map(obj -> String.valueOf(obj.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <p>根据订单ID获取用户ID</p>
     *
     * @param orderId 订单ID
     * @return 用户ID
     */
    @Override
    public String getUserId(String orderId) {
        LambdaQueryWrapper<UserOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserOrder::getOrderId, orderId).last("LIMIT 1");

        return this.getOne(queryWrapper).getUserId();
    }
}
