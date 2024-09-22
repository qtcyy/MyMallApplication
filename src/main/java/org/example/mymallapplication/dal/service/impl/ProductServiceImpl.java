package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.service.product.ICategoryService;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.example.mymallapplication.dal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private IProductsService productsService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * <p>保存商品信息</p>
     *
     * @param product 商品
     * @return 保存状态
     */
    @Override
    public SaResult saveProduct(Products product) {
        if (productsService.saveProduct(product)) {
            return SaResult.ok("保存成功！");
        }

        return SaResult.error("保存失败!");
    }

    /**
     * <p>根据产品名搜索产品信息</p>
     *
     * @param name 产品名
     * @return 含有关键字的产品信息
     */
    @Override
    public SaResult getProductsLike(String name) {
        List<Products> products = productsService.getProductsLike(name);
        if (products == null) {
            return SaResult.error("未找到");
        }
        return SaResult.ok("获取成功！").setData(products);
    }

    /**
     * <p>更新产品信息</p>
     *
     * @param newProduct 接收到的产品信息
     * @return 状态
     */
    @Override
    public SaResult changeProduct(Products newProduct) {
        Products product = productsService.getProducts(newProduct.getId());
        if (newProduct.getName() != null) {
            product.setName(newProduct.getName());
        }
        if (newProduct.getDescription() != null) {
            product.setDescription(newProduct.getDescription());
        }
        if (newProduct.getNumber() != null) {
            product.setNumber(newProduct.getNumber());
        }
        if (newProduct.getPrice() != null) {
            product.setPrice(newProduct.getPrice());
        }

        if (productsService.changeProduct(product)) {
            return SaResult.ok("更新成功!");
        }

        return SaResult.error("更新失败!");
    }

    /**
     * <p>通过ID删除商品</p>
     *
     * @param id 商品ID
     * @return 删除状态
     */
    @Override
    public SaResult deleteProduct(Long id) {
        if (productsService.deleteProduct(id)) {
            return SaResult.ok("删除成功！");
        }
        return SaResult.error("删除失败！");
    }


}
