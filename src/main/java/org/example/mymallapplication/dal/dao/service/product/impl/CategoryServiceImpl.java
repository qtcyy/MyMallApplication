package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.Category;
import org.example.mymallapplication.dal.dao.mapper.product.CategoryMapper;
import org.example.mymallapplication.dal.dao.service.product.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    /**
     * <p>根据类别名判断是否存在</p>
     *
     * @param name 类别名
     * @return 是否存在
     */
    @Override
    public boolean hasCate(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, name);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>根据ID判断类别是否存在</p>
     *
     * @param id 类别ID
     * @return 是否存在
     */
    @Override
    public boolean hasCate(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, id);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>保存类别信息</p>
     *
     * @param category 类别信息
     * @return 保存状态
     */
    @Override
    public boolean saveCate(Category category) {
        return this.save(category);
    }

    /**
     * <p>删除类别</p>
     *
     * @param id 类别ID
     * @return 删除状态
     */
    @Override
    public boolean deleteCate(Long id) {
        return this.removeById(id);
    }

    /**
     * <p>根据字符串匹配种类</p>
     *
     * @param name 字符串
     * @return 种类列表
     */
    @Override
    public List<Category> getCategories(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Category::getName, name)
                .or()
                .like(Category::getDescription, name);

        return this.list(queryWrapper);
    }

    /**
     * <p>更新类别信息</p>
     *
     * @param id       类别ID
     * @param category 类别实体类
     * @return 更新状态
     */
    @Override
    public boolean updateCategory(Long id, Category category) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId, id);

        return this.update(category, updateWrapper);
    }

}
