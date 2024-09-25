package org.example.mymallapplication;

import org.example.mymallapplication.dal.dao.service.person.IPermissionsService;
import org.example.mymallapplication.dal.dao.service.person.IRolesService;
import org.example.mymallapplication.dal.dao.service.person.IUserRoleService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    }

}
