package org.example.mymallapplication.dal.dao.entity.commit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.info.UpdateInfo;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-30
 */
@TableName("product_reviews")
@Schema(name = "ProductReviews对象", description = "")
@Getter
@Setter
public class ProductReviews extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String productId;

    private String userId;

    private Integer rating;

    private String content;

    private String parentId;

    private Integer likeCount;


    @Override
    public String toString() {
        return "ProductReviews{" +
                "id = " + id +
                ", productId = " + productId +
                ", userId = " + userId +
                ", rating = " + rating +
                ", content = " + content +
                ", createTime = " + super.getCreateTime() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateTime() +
                ", deleted = " + super.getDeleted() +
                ", parentId = " + parentId +
                "}";
    }
}
