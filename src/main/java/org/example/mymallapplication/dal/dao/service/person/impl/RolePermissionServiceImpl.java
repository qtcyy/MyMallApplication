package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.RolePermission;
import org.example.mymallapplication.dal.dao.mapper.person.RolePermissionMapper;
import org.example.mymallapplication.dal.dao.service.person.IRolePermissionService;
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
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    /**
     * <p>获取权限ID列表</p>
     *
     * @param roleIds 角色ID列表
     * @return 权限ID列表
     */
    @Override
    public List<Long> getPermissionIds(List<Long> roleIds) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds).select(RolePermission::getPermissionId);

        return this.baseMapper.selectObjs(queryWrapper).stream()
                .map(obj -> Long.valueOf(obj.toString()))
                .collect(Collectors.toList());
    }
}
