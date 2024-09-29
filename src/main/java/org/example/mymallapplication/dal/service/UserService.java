package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.ChangePwdRequest;
import org.example.mymallapplication.dal.vo.request.ConfirmOrderRequest;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;

public interface UserService {

    SaResult userLogin(UserLoginRequest request);

    SaResult userRegister(UserRegisterRequest request);

    SaResult changePwd(ChangePwdRequest request);

    SaResult rechargeBalance(double money);

    SaResult withdrawBalance(double money);

    SaResult getUserInfo();

    SaResult setAddress(String address);

    SaResult confirmOrder(ConfirmOrderRequest request);
}
