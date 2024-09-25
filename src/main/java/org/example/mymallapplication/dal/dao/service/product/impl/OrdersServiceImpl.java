package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.mapper.product.OrdersMapper;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.springframework.stereotype.Service;

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

    /**
     * <p>获取订单列表</p>
     *
     * @param orderIds 订单ID列表
     * @return 订单实体类列表
     */
    @Override
    public List<Orders> getOrders(List<Long> orderIds) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Orders::getId, orderIds);

        return this.list(queryWrapper);
    }
}
