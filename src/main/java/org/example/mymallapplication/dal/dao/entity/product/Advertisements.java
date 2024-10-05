package org.example.mymallapplication.dal.dao.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.info.UpdateInfo;
import org.example.mymallapplication.dal.enums.AdStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-05
 */
@Schema(name = "Advertisements对象", description = "")
@Getter
@Setter
public class Advertisements extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @NotBlank(message = "ID不能为空")
    private String id;

    private String title;

    private String imageUrl;

    private String redirectUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer priority;

    private AdStatus status;

    @Override
    public String toString() {
        return "Advertisements{" +
                "id = " + id +
                ", title = " + title +
                ", imageUrl = " + imageUrl +
                ", redirectUrl = " + redirectUrl +
                ", startTime = " + startTime +
                ", endTime = " + endTime +
                ", priority = " + priority +
                ", status = " + status +
                ", createTime = " + super.getCreateTime() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateBy() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
