package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.mapper.person.FrontendUsersMapper;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
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
public class FrontendUsersServiceImpl extends ServiceImpl<FrontendUsersMapper, FrontendUsers> implements IFrontendUsersService {
    /**
     * <p>是否存在用户</p>
     *
     * @param username 用户名
     * @return 是否存在
     */
    @Override
    public boolean hasUser(String username) {
        LambdaQueryWrapper<FrontendUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrontendUsers::getUsername, username);
        return this.count(queryWrapper) > 0;
    }

    /**
     * <p>根据用户名获取ID</p>
     *
     * @param username 用户名
     * @return 用户ID
     */
    @Override
    public Long getUserIdByName(String username) {
        LambdaQueryWrapper<FrontendUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrontendUsers::getUsername, username);
        return this.getOne(queryWrapper).getId();
    }

    /**
     * <p>根据用户名获取用户实体类</p>
     *
     * @param username 用户名
     * @return 用户实体类
     */
    @Override
    public FrontendUsers getFrontendUsers(String username) {
        LambdaQueryWrapper<FrontendUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrontendUsers::getUsername, username);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>根据ID获取用户实体来</p>
     *
     * @param id 用户ID
     * @return 用户实体类
     */
    @Override
    public FrontendUsers getFrontendUsers(Long id) {
        LambdaQueryWrapper<FrontendUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrontendUsers::getId, id);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>更新用户地址</p>
     *
     * @param id      用户ID
     * @param address 地址
     * @return 更新状态
     */
    @Override
    public boolean updateAddress(Long id, String address) {
        LambdaUpdateWrapper<FrontendUsers> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FrontendUsers::getId, id).set(FrontendUsers::getAddress, address);

        return this.update(updateWrapper);
    }
}
