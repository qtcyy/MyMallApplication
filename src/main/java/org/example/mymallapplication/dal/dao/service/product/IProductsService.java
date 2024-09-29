package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Products;

import java.util.List;

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

    List<Products> getProductsLike(String name);

    boolean changeProduct(Products product);

    boolean deleteProduct(String id);
}
