package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.service.AdminService;
import org.example.mymallapplication.dal.vo.request.AdminLoginRequest;
import org.example.mymallapplication.dal.vo.request.AdminRegisterRequest;
import org.example.mymallapplication.dal.vo.request.ChangePwdRequest;
import org.example.mymallapplication.dal.vo.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @SaIgnore
    @PostMapping("/login")
    public SaResult adminLogin(@RequestBody AdminLoginRequest request) {
        return adminService.adminLogin(request);
    }

    @SaIgnore
    @PostMapping("/register")
    public SaResult adminRegister(@RequestBody AdminRegisterRequest request) {
        return adminService.adminRegister(request);
    }

    @SaCheckRole(value = "admin")
    @PostMapping("/change/pwd")
    public SaResult changePwd(@RequestBody ChangePwdRequest request) {
        return adminService.changePwd(request);
    }

    @SaIgnore
    @GetMapping("/logout")
    public SaResult adminLogout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return SaResult.ok("登出成功！");
        }

        return SaResult.error("未登陆!");
    }

    @SaCheckRole("admin")
    @GetMapping("/role")
    public List<String> getLoginRole() {
        return adminService.getRole();
    }

    @SaCheckRole("admin")
    @GetMapping("/permission")
    public List<String> getLoginPermission() {
        return adminService.getPermission();
    }

    @SaCheckPermission(value = {"change", "add"}, mode = SaMode.AND)
    @PostMapping("/changeUser/{mode}")
    public SaResult changeUserInfo(@PathVariable String mode, @RequestBody UpdateUserRequest request) {
        return adminService.changeUserInfo(mode, request);
    }
}
