package org.example.mymallapplication;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.mymallapplication.dal.dao.entity.person.*;
import org.example.mymallapplication.dal.dao.service.person.*;
import org.example.mymallapplication.dal.service.DBService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataAdd {
    @Autowired
    IRolesService rolesService;
    @Autowired
    IPermissionsService permissionsService;
    @Autowired
    IRolePermissionService rolePermissionService;
    @Autowired
    IUsersService usersService;
    @Autowired
    IAdminBalanceService adminBalanceService;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IFrontendUsersService frontendUsersService;
    @Autowired
    IBalanceService balanceService;
    @Autowired
    DBService dbService;

    @Test
    void roleInsert() {
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Roles::getRole, "root");
        Roles role = rolesService.getOne(queryWrapper);

        String roleId = role.getId();

        List<String> permissionIds = permissionsService.list().stream()
                .map(Permissions::getId).toList();
        List<RolePermission> entities = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            entities.add(rolePermission);
        }

        rolePermissionService.saveBatch(entities);
    }

    @Test
    void adminInsert() {
        List<String> permissions = new ArrayList<>() {{
            add("read");
            add("add");
            add("change");
            add("delete");
        }};

        Roles role = new Roles();
        role.setRole("admin");
        rolesService.save(role);
        String roleId = role.getId();
        LambdaQueryWrapper<Permissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permissions::getPermission, permissions);

        List<String> permissionIds = permissionsService.list(queryWrapper).stream()
                .map(Permissions::getId).toList();

        List<RolePermission> entities = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            entities.add(rolePermission);
        }

        rolePermissionService.saveBatch(entities);
    }

    @Test
    void permissionInsert() {
        List<String> permissions = new ArrayList<>() {{
            add("read");
            add("add");
            add("change");
            add("delete");
            add("root.drop");
            add("root.update");
        }};
        List<Permissions> permissionsList = new ArrayList<>();
        for (String permission : permissions) {
            Permissions entity = new Permissions();
            entity.setPermission(permission);
            permissionsList.add(entity);
        }

        permissionsService.saveBatch(permissionsList);
    }

    @Test
    void addAdmin() {
        Users admin = new Users();
        admin.setUsername("qtcyy");
        admin.setPassword(SaSecureUtil.sha256("002844"));
        usersService.save(admin);
        String adminId = admin.getId();

        AdminBalance adminBalance = new AdminBalance();
        adminBalance.setAdminId(adminId);
        adminBalanceService.save(adminBalance);


        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Roles::getRole, "root").or().eq(Roles::getRole, "admin");
        List<String> roleIds = rolesService.list(queryWrapper).stream()
                .map(Roles::getId).toList();

        List<UserRole> entities = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(adminId);
            userRole.setRoleId(roleId);
            entities.add(userRole);
        }


        userRoleService.saveBatch(entities);
    }

    @Test
    void addUserBalance() {
        LambdaQueryWrapper<FrontendUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrontendUsers::getUsername, "lx");
        FrontendUsers user = frontendUsersService.getOne(queryWrapper);
        String userId = user.getId();

        Balance balance = new Balance();
        balance.setUserId(userId);
        balanceService.save(balance);
    }

    @Test
    void passwdTest() {
        System.out.println(SaSecureUtil.sha256("123456"));
    }
}
