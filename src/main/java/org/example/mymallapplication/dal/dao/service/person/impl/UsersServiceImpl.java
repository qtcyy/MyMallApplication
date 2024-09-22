package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.Users;
import org.example.mymallapplication.dal.dao.mapper.person.UsersMapper;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    /**
     * <p>通过用户名获取用户ID</p>
     *
     * @param username 用户名
     * @return 用户ID
     */
    @Override
    public Long getIdByName(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);
        return this.getOne(queryWrapper).getId();
    }

    /**
     * <p>判断是否存在用户名</p>
     *
     * @param username 用户名
     * @return 是否存在
     */
    @Override
    public boolean hasUser(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>判断是否存在用户ID</p>
     *
     * @param id 用户ID
     * @return 是否存在
     */
    @Override
    public boolean hasUser(Long id) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getId, id);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>根据用户名获取用户实体类</p>
     *
     * @param username 用户名
     * @return 用户实体类
     */
    @Override
    public Users getUser(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);

        return this.getOne(queryWrapper);
    }
}
