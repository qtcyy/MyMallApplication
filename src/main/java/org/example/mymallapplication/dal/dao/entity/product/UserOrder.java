package org.example.mymallapplication.dal.dao.entity.product;

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
 * @since 2024-09-20
 */
@TableName("user_order")
@Schema(name = "UserOrder对象", description = "")
@Getter
@Setter
public class UserOrder extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String userId;

    private String orderId;

    @Override
    public String toString() {
        return "UserOrder{" +
                "id = " + id +
                ", userId = " + userId +
                ", orderId = " + orderId +
                "}";
    }
}
