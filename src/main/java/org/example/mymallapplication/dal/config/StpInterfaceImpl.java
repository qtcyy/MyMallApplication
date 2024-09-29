package org.example.mymallapplication.dal.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private DBService dbService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        String userId = (String) o;

        return dbService.getPermissions(userId);
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        String userId = (String) o;

        return dbService.getRoles(userId);
    }
}
