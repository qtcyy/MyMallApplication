package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.ProductCate;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
public interface IProductCateService extends IService<ProductCate> {

    boolean hasGroup(String cateId, String productId);

    boolean saveGroup(String cateId, String productId);

    boolean deleteByCate(String cateId);

    boolean deleteGroup(String cateId, String productId);

    List<String> getProductIds(List<String> cateIds);

    List<ProductCate> getProducts(List<String> ids);
}
