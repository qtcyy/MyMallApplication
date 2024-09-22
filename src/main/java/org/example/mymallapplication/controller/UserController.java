package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @SaIgnore
    @RequestMapping("/login")
    public SaResult doLogin(@RequestBody UserLoginRequest request) {
        return userService.userLogin(request);
    }

    @SaIgnore
    @RequestMapping("/register")
    public SaResult doRegister(@RequestBody UserRegisterRequest request) {
        return userService.userRegister(request);
    }

    @SaCheckLogin
    @GetMapping("/info/get")
    public SaResult getLoginUserInfo() {
        return userService.getUserInfo();
    }

    @SaCheckLogin
    @PostMapping("/info/address")
    public SaResult setAddress(@RequestParam String address) {
        return userService.setAddress(address);
    }
}
