package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.UserRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface IUserRoleService extends IService<UserRole> {

    List<Long> getRoleIds(Long userId);
}
