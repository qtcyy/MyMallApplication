package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.UserRole;
import org.example.mymallapplication.dal.dao.mapper.person.UserRoleMapper;
import org.example.mymallapplication.dal.dao.service.person.IUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    /**
     * <p>获取角色ID列表</p>
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<String> getRoleIds(String userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId).select(UserRole::getRoleId);

        return this.baseMapper.selectObjs(queryWrapper).stream()
                .map(obj -> String.valueOf(obj.toString()))
                .collect(Collectors.toList());
    }
}
