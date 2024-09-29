package org.example.mymallapplication.dal.dao.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.info.UpdateInfo;
import org.example.mymallapplication.dal.enums.State;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Schema(name = "Orders对象", description = "")
@Getter
@Setter
public class Orders extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private LocalDateTime time;

    private int number;

    @TableField("state")
    private State state;

    private Double price;

    private String address;

    private String remark;

    private LocalDateTime shippingTime;


    @Override
    public String toString() {
        return "Orders{" +
                "id = " + id +
                ", time = " + time +
                ", state = " + state +
                ", address = " + address +
                ", remark = " + remark +
                ", createTime = " + super.getCreateTime() +
                ", updateTime = " + super.getUpdateTime() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
