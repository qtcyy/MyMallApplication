package org.example.mymallapplication.dal.dao.service.commit;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.commit.ReviewImages;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-05
 */
public interface IReviewImagesService extends IService<ReviewImages> {

    List<ReviewImages> getReviewImages(String reviewId);
}
