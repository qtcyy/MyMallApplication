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
@TableName("review_likes")
@Schema(name = "ReviewLikes对象", description = "")
@Getter
@Setter
public class ReviewLikes extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String reviewId;

    private String userId;

    @Override
    public String toString() {
        return "ReviewLikes{" +
                "id = " + id +
                ", reviewId = " + reviewId +
                ", userId = " + userId +
                ", createTime = " + super.getCreateTime() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateBy() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
