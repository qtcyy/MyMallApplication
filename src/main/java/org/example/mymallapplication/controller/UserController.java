package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.*;
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

    @SaCheckLogin
    @PostMapping("/product/commit/")
    public SaResult writeCommit(@Valid @RequestBody WriteCommitRequest request) {
        return userService.writeCommit(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit/reply")
    public SaResult replyCommit(@Valid @RequestBody CommitReplyRequest request) {
        return userService.replyCommit(request);
    }

    @SaCheckLogin
    @GetMapping("/product/commit/like/{id}")
    public SaResult likeCommit(@PathVariable String id) {
        return userService.likeCommit(id);
    }

    @SaIgnore
    @GetMapping("/product/commit/show/{productId}")
    public SaResult commit(
            @PathVariable String productId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return userService.getCommit(productId, page, size);
    }
}
