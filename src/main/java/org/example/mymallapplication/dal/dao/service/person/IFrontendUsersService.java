package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface IFrontendUsersService extends IService<FrontendUsers> {

    boolean hasUser(String username);

    String getUserIdByName(String username);

    FrontendUsers getFrontendUsers(String str);

    boolean updateAddress(String id, String address);
}
