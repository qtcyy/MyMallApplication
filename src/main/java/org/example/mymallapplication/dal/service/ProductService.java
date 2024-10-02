package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.vo.request.ShippingRequest;

/**
 * @author chengyiyang
 */
public interface ProductService {
    SaResult saveProduct(Products product);

    SaResult getProductsLike(String name);

    SaResult getProductsPage(String name, int page, int size);

    SaResult changeProduct(Products newProduct);

    SaResult deleteProduct(String id);

    SaResult shipProduct(ShippingRequest request);
}
