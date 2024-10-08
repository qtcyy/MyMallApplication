package org.example.mymallapplication.dal.dao.service.commit.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.commit.ProductReviews;
import org.example.mymallapplication.dal.dao.mapper.commit.ProductReviewsMapper;
import org.example.mymallapplication.dal.dao.service.commit.IProductReviewsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-30
 */
@Service
public class ProductReviewsServiceImpl extends ServiceImpl<ProductReviewsMapper, ProductReviews> implements IProductReviewsService {

    /**
     * 获取评论
     *
     * @param id 评论ID
     * @return 评论实体类
     */
    @Override
    public ProductReviews getReview(String id) {
        LambdaQueryWrapper<ProductReviews> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductReviews::getId, id).last("limit 1");

        return this.getOne(wrapper);
    }

    /**
     * 获取分页
     *
     * @param page      分页信息
     * @param productId 产品ID
     * @return 分页后的字页面
     */
    @Override
    public IPage<ProductReviews> getMainReviews(String productId, int page, int size) {
        Page<ProductReviews> reviewPage = new Page<>(page, size);
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getProductId, productId)
                .isNull(ProductReviews::getParentId)
                .orderByDesc(ProductReviews::getCreateTime);
        //分页查询
        return this.page(reviewPage, reviewsWrapper);
    }

    /**
     * 获取用户评论
     *
     * @param page   分页信息
     * @param userId 用户ID
     * @return 用户评论
     */
    @Override
    public IPage<ProductReviews> getSelfReviews(Page<ProductReviews> page, String userId) {
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getUserId, userId)
                .isNull(ProductReviews::getParentId)
                .orderByDesc(ProductReviews::getCreateTime);
        return this.page(page, reviewsWrapper);
    }

    /**
     * 根据产品ID获取评论
     *
     * @param productId 产品ID
     * @return 评论列表
     */
    @Override
    public List<ProductReviews> getReviewsByProductId(String productId) {
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getProductId, productId);
        return this.list(reviewsWrapper);
    }

    /**
     * 根据父评论ID获取子评论
     *
     * @param parentId 父评论ID
     * @return 子评论列表
     */
    @Override
    public List<ProductReviews> getListByParent(String parentId) {
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getParentId, parentId);

        return this.list(reviewsWrapper);
    }

    /**
     * 批量获取子评论
     *
     * @param parentIds 父评论ID列表
     * @return 子评论
     */
    @Override
    public Map<String, List<ProductReviews>> getRepliesByParentIds(List<String> parentIds) {
        if (parentIds == null || parentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.in(ProductReviews::getParentId, parentIds);

        List<ProductReviews> replies = this.list(reviewsWrapper);
        return replies.stream().collect(Collectors.groupingBy(ProductReviews::getParentId));
    }

    /**
     * 获取差评
     *
     * @param productId 产品ID
     * @param page      分页信息
     * @return 差评
     */
    @Override
    public IPage<ProductReviews> getBadReviews(String productId, Page<ProductReviews> page) {
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getProductId, productId)
                .le(ProductReviews::getRating, 2)
                .orderByDesc(ProductReviews::getCreateTime);
        return this.page(page, reviewsWrapper);
    }

    /**
     * 获取好评
     *
     * @param productId 产品ID
     * @param page      分页信息
     * @return 好评
     */
    @Override
    public IPage<ProductReviews> getGoodReviews(String productId, Page<ProductReviews> page) {
        LambdaQueryWrapper<ProductReviews> reviewsWrapper = new LambdaQueryWrapper<>();
        reviewsWrapper.eq(ProductReviews::getProductId, productId)
                .ge(ProductReviews::getRating, 4)
                .orderByDesc(ProductReviews::getCreateTime);
        return this.page(page, reviewsWrapper);
    }
}
