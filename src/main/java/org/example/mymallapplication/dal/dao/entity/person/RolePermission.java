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
 * @since 2024-09-18
 */
@TableName("role_permission")
@Schema(name = "RolePermission对象", description = "")
@Getter
@Setter
public class RolePermission extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String roleId;

    private String permissionId;


    @Override
    public String toString() {
        return "RolePermission{" +
                "id = " + id +
                ", roleId = " + roleId +
                ", permissionId = " + permissionId +
                ", deleted = " + super.getDeleted() +
                "}";
    }
}
