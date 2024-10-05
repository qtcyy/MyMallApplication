package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.entity.product.Sales;
import org.example.mymallapplication.dal.dao.mapper.product.SalesMapper;
import org.example.mymallapplication.dal.dao.service.product.ISalesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-03
 */
@Service
public class SalesServiceImpl extends ServiceImpl<SalesMapper, Sales> implements ISalesService {

    /**
     * 按销量降序
     *
     * @param salesPage 页面信息
     * @param products  产品ID列表
     * @return 分页信息
     */
    @Override
    public IPage<Sales> getSalesPage(Page<Sales> salesPage, @NotNull List<Products> products) {
        LambdaQueryWrapper<Sales> queryWrapper = new LambdaQueryWrapper<>();
        List<String> productIds = products.stream()
                .map(Products::getId).toList();
        queryWrapper.in(Sales::getProductId, productIds)
                .orderBy(true, false, Sales::getCount);

        return this.page(salesPage, queryWrapper);
    }

    /**
     * 按销量升序
     *
     * @param salesPage 页面信息
     * @param products  产品ID列表
     * @return 分页信息
     */
    @Override
    public IPage<Sales> geySalesPageDsc(Page<Sales> salesPage, @NotNull List<Products> products) {
        LambdaQueryWrapper<Sales> queryWrapper = new LambdaQueryWrapper<>();
        List<String> productIds = products.stream()
                .map(Products::getId).toList();
        queryWrapper.in(Sales::getProductId, productIds)
                .orderBy(true, true, Sales::getCount);

        return this.page(salesPage, queryWrapper);
    }
}
