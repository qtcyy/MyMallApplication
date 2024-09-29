package org.example.mymallapplication;

import org.example.mymallapplication.dal.dao.service.person.*;
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
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private IAdminBalanceService adminBalanceService;

    @Test
    void contextLoads() {

    }

}
