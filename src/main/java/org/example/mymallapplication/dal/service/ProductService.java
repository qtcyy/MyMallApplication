package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Products;

public interface ProductService {
    SaResult saveProduct(Products product);

    SaResult getProductsLike(String name);

    SaResult changeProduct(Products newProduct);

    SaResult deleteProduct(Long id);
}
