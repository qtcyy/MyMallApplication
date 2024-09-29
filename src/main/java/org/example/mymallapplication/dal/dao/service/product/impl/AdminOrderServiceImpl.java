package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.AdminOrder;
import org.example.mymallapplication.dal.dao.mapper.product.AdminOrderMapper;
import org.example.mymallapplication.dal.dao.service.product.IAdminOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-23
 */
@Service
public class AdminOrderServiceImpl extends ServiceImpl<AdminOrderMapper, AdminOrder> implements IAdminOrderService {

    /**
     * <p>根据订单ID获取对应管理员ID</p>
     *
     * @param orderId 订单ID
     * @return 管理员ID
     */
    @Override
    public String getAdminId(String orderId) {
        LambdaQueryWrapper<AdminOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminOrder::getOrderId, orderId);

        return this.getOne(queryWrapper).getAdminId();
    }

    /**
     * <p>根据订单ID列表获取映射表实体类分页</p>
     *
     * @param page     分页对象
     * @param orderIds 订单ID列表
     * @return 实体类分页
     */
    @Override
    public IPage<AdminOrder> getEntitiesPage(IPage<AdminOrder> page, List<Long> orderIds) {
        LambdaQueryWrapper<AdminOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AdminOrder::getOrderId, orderIds);

        return this.page(page, queryWrapper);
    }

    /**
     * <p>获取管理员ID映射</p>
     *
     * @param orderIds 订单ID列表
     * @return 管理员ID映射
     */
    @Override
    public List<AdminOrder> getAdminIds(List<String> orderIds) {
        LambdaQueryWrapper<AdminOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AdminOrder::getOrderId, orderIds);

        return this.list(queryWrapper);
    }
}
