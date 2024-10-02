package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Products;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
public interface IProductsService extends IService<Products> {

    boolean saveProduct(Products product);

    boolean hasProduct(String str);

    List<String> getExistingProductIds(List<String> productIds);

    Products getProducts(String str);

    List<Products> getProducts(List<String> ids);

    Map<String, Products> getProductsIdMap(List<String> ids);

    List<Products> getProductsLike(String name);

    IPage<Products> getProductPage(Page<Products> page, String name);

    boolean changeProduct(Products product);

    boolean deleteProduct(String id);
}
