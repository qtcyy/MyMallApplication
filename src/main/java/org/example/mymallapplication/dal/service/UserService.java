package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;

public interface UserService {

    SaResult userLogin(UserLoginRequest request);

    SaResult userRegister(UserRegisterRequest request);

    SaResult getUserInfo();

    SaResult setAddress(String address);
}
