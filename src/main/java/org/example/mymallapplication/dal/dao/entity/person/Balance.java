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
 * @since 2024-09-25
 */
@Schema(name = "Balance对象", description = "")
@Getter
@Setter
public class Balance extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String userId;

    private Double balance;

    @Override
    public String toString() {
        return "Balance{" +
                "id = " + id +
                ", userId = " + userId +
                ", balance = " + balance +
                ", createTime = " + super.getCreateTime() +
                ", updateTime = " + super.getUpdateTime() +
                "}";
    }
}
