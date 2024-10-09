package org.example.mymallapplication.dal.dao.mapper.person;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.mymallapplication.dal.dao.entity.person.Permissions;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface PermissionsMapper extends BaseMapper<Permissions> {

    @Select("select distinct permissions.permission from user_role " +
            "inner join role_permission on user_role.role_id = role_permission.role_id " +
            "inner join permissions on role_permission.permission_id = permissions.id "
            + "where user_role.user_id = #{userId}")
    List<String> getPermissionsByUserId(@Param("userId") String userId);
}
