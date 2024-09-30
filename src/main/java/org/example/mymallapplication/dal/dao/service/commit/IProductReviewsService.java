package org.example.mymallapplication.dal.dao.service.commit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.commit.ProductReviews;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-30
 */
public interface IProductReviewsService extends IService<ProductReviews> {

    public ProductReviews getReview(String id);

    IPage<ProductReviews> getMainReviews(String productId, int page, int size);

    List<ProductReviews> getListByParent(String parentId);

    Map<String, List<ProductReviews>> getRepliesByParentIds(List<String> parentIds);
}
