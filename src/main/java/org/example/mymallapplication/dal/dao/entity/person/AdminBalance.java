package org.example.mymallapplication.dal.dao.entity.person;

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
 * @since 2024-09-28
 */
@TableName("admin_balance")
@Schema(name = "AdminBalance对象", description = "")
@Getter
@Setter
public class AdminBalance extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String adminId;

    private Double balance;

    @Override
    public String toString() {
        return "AdminBalance{" +
                "id = " + id +
                ", adminId = " + adminId +
                ", balance = " + balance +
                ", createTime = " + super.getCreateTime() +
                ", updateTime = " + super.getUpdateTime() +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
