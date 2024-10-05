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
 * @since 2024-10-05
 */
@TableName("review_images")
@Schema(name = "ReviewImages对象", description = "")
@Getter
@Setter
public class ReviewImages extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String reviewId;

    private String imageUrl;

    @Override
    public String toString() {
        return "ReviewImages{" +
                "id = " + id +
                ", reviewId = " + reviewId +
                ", imageUrl = " + imageUrl +
                ", createTime = " + super.getCreateTime() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateBy() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
