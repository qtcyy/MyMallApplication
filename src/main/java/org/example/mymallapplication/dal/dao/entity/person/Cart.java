package org.example.mymallapplication.dal.dao.entity.person;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

@Getter
@Setter
@Schema(name = "Cart对象", description = "")
public class Cart extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String userId;

    private String productId;

    private Integer quantity;

    @Override
    public String toString() {
        return "Cart{" +
                "id = " + id +
                ", userId = " + userId +
                ", productId = " + productId +
                ", createTime = " + super.getCreateBy() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateBy() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
