package org.example.mymallapplication.dal.service.impl;

import org.example.mymallapplication.dal.dao.service.person.IPermissionsService;
import org.example.mymallapplication.dal.dao.service.person.IRolePermissionService;
import org.example.mymallapplication.dal.dao.service.person.IRolesService;
import org.example.mymallapplication.dal.dao.service.person.IUserRoleService;
import org.example.mymallapplication.dal.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServiceImpl implements DBService {

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRolesService rolesService;
    @Autowired
    private IRolePermissionService rolePermissionService;
    @Autowired
    private IPermissionsService permissionsService;

    /**
     * <p>获取角色字符串列表</p>
     *
     * @param userId 用户ID列表
     * @return 角色字符串列表
     */
    @Override
    public List<String> getRoles(Long userId) {
        List<Long> roleIds = userRoleService.getRoleIds(userId);
        return rolesService.getRoles(roleIds);
    }

    /**
     * <p>获取权限字符串列表</p>
     *
     * @param userId 用户ID
     * @return 权限字符串列表
     */
    @Override
    public List<String> getPermissions(Long userId) {
        List<Long> roleIds = userRoleService.getRoleIds(userId);
        List<Long> permissionIds = rolePermissionService.getPermissionIds(roleIds);

        return permissionsService.getPermissions(permissionIds);
    }
}