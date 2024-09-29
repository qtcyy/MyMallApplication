package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.ProductCate;
import org.example.mymallapplication.dal.dao.mapper.product.ProductCateMapper;
import org.example.mymallapplication.dal.dao.service.product.IProductCateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Service
public class ProductCateServiceImpl extends ServiceImpl<ProductCateMapper, ProductCate> implements IProductCateService {

    /**
     * <p>查找是否存在分组</p>
     *
     * @param cateId    类别ID
     * @param productId 产品ID
     * @return 是否存在
     */
    @Override
    public boolean hasGroup(String cateId, String productId) {
        LambdaQueryWrapper<ProductCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCate::getCategoryId, cateId)
                .eq(ProductCate::getProductId, productId);

        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>保存映射信息</p>
     *
     * @param cateId    类别ID
     * @param productId 商品ID
     * @return 保存状态
     */
    @Override
    public boolean saveGroup(String cateId, String productId) {
        ProductCate productCate = new ProductCate();
        productCate.setCategoryId(cateId);
        productCate.setProductId(productId);
        return this.save(productCate);
    }

    /**
     * <p>根据类别ID删除映射</p>
     *
     * @param cateId 类别ID
     * @return 删除状态
     */
    @Override
    public boolean deleteByCate(String cateId) {
        LambdaQueryWrapper<ProductCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCate::getCategoryId, cateId);
        return this.remove(queryWrapper);
    }

    /**
     * <p>删除分组</p>
     *
     * @param cateId    类别ID
     * @param productId 产品ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteGroup(String cateId, String productId) {
        LambdaQueryWrapper<ProductCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCate::getCategoryId, cateId)
                .eq(ProductCate::getProductId, productId);

        return this.remove(queryWrapper);
    }

    /**
     * <p>根据类别ID列表获取产品ID列表</p>
     *
     * @param cateIds 类别ID列表
     * @return 产品ID列表
     */
    @Override
    public List<String> getProductIds(List<String> cateIds) {
        LambdaQueryWrapper<ProductCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductCate::getCategoryId, cateIds);

        return this.list(queryWrapper).stream()
                .map(ProductCate::getProductId).collect(Collectors.toList());
    }

    /**
     * <p>根据产品ID列表获取映射表实体列表</p>
     *
     * @param ids 产品ID列表
     * @return 映射表实体列表
     */
    @Override
    public List<ProductCate> getProducts(List<String> ids) {
        LambdaQueryWrapper<ProductCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductCate::getProductId, ids);

        return this.list(queryWrapper);
    }
}
