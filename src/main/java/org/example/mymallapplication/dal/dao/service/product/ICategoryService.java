package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Category;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
public interface ICategoryService extends IService<Category> {

    boolean hasCate(String name);

    boolean hasCate(Long id);

    boolean saveCate(Category category);

    boolean deleteCate(Long id);

    List<Category> getCategories(String name);

    boolean updateCategory(Long id, Category category);
}
