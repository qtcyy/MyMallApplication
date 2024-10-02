package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.mapper.product.OrdersMapper;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.example.mymallapplication.dal.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    OrdersMapper mapper;

    /**
     * <p>获取订单列表</p>
     *
     * @param orderIds 订单ID列表
     * @return 订单实体类列表
     */
    @Override
    public List<Orders> getOrders(List<String> orderIds) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Orders::getId, orderIds);

        return this.list(queryWrapper);
    }

    /**
     * <p>根据状态获取分页后的order</p>
     *
     * @param state 状态
     * @return 分页的order
     */
    @Override
    public IPage<Orders> getOrderPageByState(IPage<Orders> page, State state) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getState, state);

        return this.page(page, queryWrapper);
    }

    @Override
    public List<Orders> getOrderByState(State state) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getState, state);

        return this.list(queryWrapper);
    }

    /**
     * 根据ID和状态获取订单列表
     *
     * @param orderIds 订单ID列表
     * @param state    状态
     * @return 订单列表
     */
    @Override
    public List<Orders> getOrdersWithState(List<String> orderIds, State state) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Orders::getId, orderIds).eq(Orders::getState, state);

        return this.list(queryWrapper);
    }

    /**
     * <p>根据状态获取订单</p>
     *
     * @param state 状态
     * @return 订单列表
     */
    @Override
    public List<Orders> getOrderToConfirm(State state, LocalDateTime fifteenDaysTime) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getState, state).le(Orders::getShippingTime, fifteenDaysTime);

        return this.list(queryWrapper);
    }
}
