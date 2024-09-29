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
 * @since 2024-09-20
 */
@Schema(name = "Products对象", description = "")
@Getter
@Setter
public class Products extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String description;

    private Integer number;

    private Double price;

    @Override
    public String toString() {
        return "Products{" +
                "id = " + id +
                ", name = " + name +
                ", description = " + description +
                ", number = " + number +
                ", price = " + price +
                ", createTime = " + super.getCreateTime() +
                ", updateTime = " + super.getUpdateTime() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
