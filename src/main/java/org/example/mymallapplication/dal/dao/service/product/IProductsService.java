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

    boolean hasProduct(Long id);

    boolean hasProduct(String name);

    Products getProducts(Long id);

    Products getProducts(String name);

    List<Products> getProducts(List<Long> ids);

    List<Products> getProductsLike(String name);

    boolean changeProduct(Products product);

    boolean deleteProduct(Long id);
}
