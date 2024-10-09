package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.Roles;
import org.example.mymallapplication.dal.dao.mapper.person.RolesMapper;
import org.example.mymallapplication.dal.dao.service.person.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

    @Autowired
    private RolesMapper rolesMapper;

    /**
     * <p>获取角色字符串列表</p>
     *
     * @param roleIds 角色ID列表
     * @return 角色字符串列表
     */
    @Override
    public List<String> getRoles(List<String> roleIds) {
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Roles::getId, roleIds).select(Roles::getRole);

        return this.baseMapper.selectObjs(queryWrapper).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * <p>获取用户角色</p>
     *
     * @param userId 用户ID
     * @return 用户角色列表
     */
    @Override
    public List<String> getRoles(String userId) {
        return rolesMapper.getRolesByUserId(userId);
    }
}
