package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.AdminLoginRequest;
import org.example.mymallapplication.dal.vo.request.AdminRegisterRequest;
import org.example.mymallapplication.dal.vo.request.UpdateUserRequest;

import java.util.List;

public interface AdminService {
    SaResult adminLogin(AdminLoginRequest request);

    SaResult adminRegister(AdminRegisterRequest request);

    List<String> getRole();

    List<String> getPermission();

    SaResult changeUserInfo(String mode, UpdateUserRequest request);
}
