package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.mapper.product.ProductsMapper;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>服务实现类</p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Slf4j
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements IProductsService {

    /**
     * <p>存储产品</p>
     *
     * @param product 产品信息
     * @return 是否存储成功
     */
    @Override
    public boolean saveProduct(Products product) {
        return this.save(product);
    }


    /**
     * <p>根据商品名判断是否存在</p>
     *
     * @param str 商品名
     * @return 是否存在
     */
    @Override
    public boolean hasProduct(String str) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Products::getName, str).or().eq(Products::getId, str);
        return this.count(queryWrapper) > 0;
    }


    /**
     * <p>批量查询存在的ID</p>
     *
     * @param productIds 产品ID列表
     * @return 存在的产品ID列表
     */
    @Override
    public List<String> getExistingProductIds(List<String> productIds) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Products::getId, productIds);

        return this.list(queryWrapper).stream()
                .map(Products::getId).collect(Collectors.toList());
    }

    /**
     * <p>根据商品名获取商品信息</p>
     *
     * @param str 商品名
     * @return 商品信息
     */
    @Override
    public Products getProducts(String str) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Products::getName, str).or().eq(Products::getId, str)
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    /**
     * <p>根据产品ID列表获取产品列表</p>
     *
     * @param ids 产品ID列表
     * @return 产品列表
     */
    @Override
    public List<Products> getProducts(List<String> ids) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Products::getId, ids);

        return this.list(queryWrapper);
    }

    /**
     * 根据商品ID列表获取map对
     *
     * @param ids 商品ID列表
     * @return map对
     */
    @Override
    public Map<String, Products> getProductsIdMap(List<String> ids) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Products::getId, ids);
        List<Products> products = this.list(queryWrapper);
        return products.stream().collect(Collectors.toMap(Products::getId, p -> p));
    }

    /**
     * <p>近似查询商品</p>
     *
     * @param name 商品名
     * @return 商品信息列表
     */
    @Override
    public List<Products> getProductsLike(String name) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Products::getName, name)
                .or()
                .like(Products::getDescription, name);
        return this.list(queryWrapper);
    }

    /**
     * 分页查询商品
     *
     * @param page 页面信息
     * @param name 商品名称
     * @return 分页信息
     */
    @Override
    public IPage<Products> getProductPage(Page<Products> page, String name) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Products::getName, name)
                .or().like(Products::getDescription, name);
        return this.page(page, queryWrapper);
    }

    /**
     * <p>更新产品信息</p>
     *
     * @param product 产品信息
     * @return 是否更新成功
     */
    @Override
    public boolean changeProduct(Products product) {
        return this.updateById(product);
    }

    /**
     * <p>根据ID删除商品</p>
     *
     * @param id 商品ID
     * @return 是否成功删除
     */
    @Override
    public boolean deleteProduct(String id) {
        return this.removeById(id);
    }
}
