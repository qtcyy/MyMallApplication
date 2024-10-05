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

    SaResult getProductsGroup(String groupId);

    SaResult changeProduct(Products newProduct);

    SaResult deleteProduct(String id);

    SaResult shipProduct(ShippingRequest request);

    SaResult getStar(String id);

    SaResult getBadComment(String id, int page, int size);

    SaResult getGoodComment(String id, int page, int size);

    SaResult getProductsPageDesc(String name, int page, int size);

    SaResult getProductsPageDsc(String name, int page, int size);

    SaResult getProductsPageRange(String name, int minPrice, int maxPrice, int page, int size);
}
