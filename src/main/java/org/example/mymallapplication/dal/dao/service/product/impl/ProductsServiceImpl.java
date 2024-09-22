package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.mapper.product.ProductsMapper;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * <p>根据商品ID判断是否存在</p>
     *
     * @param id 商品ID
     * @return 是否存在
     */
    @Override
    public boolean hasProduct(Long id) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Products::getId, id);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>根据商品名判断是否存在</p>
     *
     * @param name 商品名
     * @return 是否存在
     */
    @Override
    public boolean hasProduct(String name) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Products::getName, name);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>根据ID获取商品信息</p>
     *
     * @param id 用户ID
     * @return 商品
     */
    @Override
    public Products getProducts(Long id) {
        return this.getById(id);
    }

    /**
     * <p>根据商品名获取商品信息</p>
     *
     * @param name 商品名
     * @return 商品信息
     */
    @Override
    public Products getProducts(String name) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Products::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>根据产品ID列表获取产品列表</p>
     *
     * @param ids 产品ID列表
     * @return 产品列表
     */
    @Override
    public List<Products> getProducts(List<Long> ids) {
        LambdaQueryWrapper<Products> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Products::getId, ids);

        return this.list(queryWrapper);
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
    public boolean deleteProduct(Long id) {
        return this.removeById(id);
    }
}