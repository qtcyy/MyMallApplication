package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.Users;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface IUsersService extends IService<Users> {

    Long getIdByName(String username);

    boolean hasUser(String username);

    boolean hasUser(Long id);

    Users getUser(String username);
}
