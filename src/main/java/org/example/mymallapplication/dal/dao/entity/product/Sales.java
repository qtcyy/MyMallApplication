package org.example.mymallapplication.dal.dao.entity.product;

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
 * @since 2024-10-03
 */
@Schema(name = "Sales对象", description = "")
@Getter
@Setter
public class Sales extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String productId;

    private Integer count;

    @Override
    public String toString() {
        return "Sales{" +
                "id = " + id +
                ", productId = " + productId +
                ", count = " + count +
                ", createTime = " + super.getCreateTime() +
                ", createBy = " + super.getCreateBy() +
                ", updateTime = " + super.getUpdateTime() +
                ", updateBy = " + super.getUpdateBy() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
