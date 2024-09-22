package org.example.mymallapplication;

import org.example.mymallapplication.dal.dao.entity.person.UserRole;
import org.example.mymallapplication.dal.dao.service.person.IPermissionsService;
import org.example.mymallapplication.dal.dao.service.person.IRolesService;
import org.example.mymallapplication.dal.dao.service.person.IUserRoleService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MyMailApplicationTests {
    @Autowired
    private IPermissionsService permissionsService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private IRolesService rolesService;
    @Autowired
    private IUserRoleService userRoleService;

    @Test
    void contextLoads() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<String> roles = rolesService.getRoles(roleIds);
        for (String role : roles) {
            System.out.println(role);
        }
    }

    @Test
    void permissionTest() {
        List<Long> permissionIds = new ArrayList<>();
        for (Long id = 1L; id <= 5L; id++) {
            permissionIds.add(id);
        }
        List<String> permissions = permissionsService.getPermissions(permissionIds);

        for (String permission : permissions) {
            System.out.println(permission);
        }
    }

    @Test
    void roleInsert() {
        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(2L);

        userRoleService.save(userRole);
    }

}
