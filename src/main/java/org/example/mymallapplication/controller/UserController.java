package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.ChangePwdRequest;
import org.example.mymallapplication.dal.vo.request.ConfirmOrderRequest;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @SaIgnore
    @PostMapping("/login")
    public SaResult doLogin(@RequestBody UserLoginRequest request) {
        return userService.userLogin(request);
    }

    @SaIgnore
    @PostMapping("/register")
    public SaResult doRegister(@RequestBody UserRegisterRequest request) {
        return userService.userRegister(request);
    }

    @SaCheckLogin
    @GetMapping("/isLogin")
    public SaResult isLogin() {
        if (StpUtil.getRoleList().equals(Collections.emptyList())) {
            return SaResult.ok("普通用户登陆");
        } else {
            return SaResult.error("管理员禁用");
        }
    }

    @SaCheckLogin
    @PostMapping("/change/pwd")
    public SaResult changePwd(ChangePwdRequest request) {
        return userService.changePwd(request);
    }

    @SaCheckLogin
    @GetMapping("/info/get")
    public SaResult getLoginUserInfo() {
        return userService.getUserInfo();
    }

    @SaCheckLogin
    @GetMapping("/balance/recharge/{money}")
    public SaResult rechargeBalance(@PathVariable double money) {
        return userService.rechargeBalance(money);
    }

    @SaCheckLogin
    @GetMapping("/balance/withdraw/{money}")
    public SaResult withdrawBalance(@PathVariable double money) {
        return userService.withdrawBalance(money);
    }

    @SaCheckLogin
    @PostMapping("/info/address/{address}")
    public SaResult setAddress(@PathVariable String address) {
        return userService.setAddress(address);
    }

    @SaCheckLogin
    @PostMapping("/order/confirm")
    public SaResult confirmOrder(@RequestBody ConfirmOrderRequest request) {
        return userService.confirmOrder(request);
    }
}
