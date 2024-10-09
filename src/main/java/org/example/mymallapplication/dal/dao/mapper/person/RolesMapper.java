package org.example.mymallapplication.dal.dao.mapper.person;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.mymallapplication.dal.dao.entity.person.Roles;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface RolesMapper extends BaseMapper<Roles> {

    @Select("select distinct roles.role from user_role" +
            " inner join roles on user_role.role_id = roles.id" +
            " where user_role.user_id=#{userId}")
    List<String> getRolesByUserId(String userId);
}
