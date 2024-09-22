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

    boolean hasGroup(Long cateId, Long productId);

    boolean saveGroup(Long cateId, Long productId);

    boolean deleteByCate(Long cateId);

    boolean deleteGroup(Long cateId, Long productId);

    List<Long> getProductIds(List<Long> cateIds);
}
