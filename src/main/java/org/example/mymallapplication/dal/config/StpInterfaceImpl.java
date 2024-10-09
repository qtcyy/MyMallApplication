package org.example.mymallapplication.dal.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.dao.service.person.IPermissionsService;
import org.example.mymallapplication.dal.dao.service.person.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chengyiyang
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private IPermissionsService permissionsService;
    @Autowired
    private IRolesService rolesService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        String userId = (String) o;

        return permissionsService.getPermissions(userId);
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        String userId = (String) o;

        return rolesService.getRoles(userId);
    }
}
