package org.example.mymallapplication.dal.dao.service.commit.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.commit.ReviewImages;
import org.example.mymallapplication.dal.dao.mapper.commit.ReviewImagesMapper;
import org.example.mymallapplication.dal.dao.service.commit.IReviewImagesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-05
 */
@Service
public class ReviewImagesServiceImpl extends ServiceImpl<ReviewImagesMapper, ReviewImages> implements IReviewImagesService {

    /**
     * 获取评论图片
     *
     * @param reviewId 评论ID
     * @return 图片列表
     */
    @Override
    public List<ReviewImages> getReviewImages(String reviewId) {
        LambdaQueryWrapper<ReviewImages> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReviewImages::getReviewId, reviewId)
                .orderBy(true, true, ReviewImages::getUpdateTime);

        return list(queryWrapper);
    }
}
