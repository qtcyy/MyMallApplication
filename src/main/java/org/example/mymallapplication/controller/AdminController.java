package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.mymallapplication.dal.dao.entity.product.Advertisements;
import org.example.mymallapplication.dal.service.AdminService;
import org.example.mymallapplication.dal.vo.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chengyiyang
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * 管理员登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    @SaIgnore
    @PostMapping("/login")
    public SaResult adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return adminService.adminLogin(request);
    }

    /**
     * 管理员注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @SaIgnore
    @PostMapping("/register")
    public SaResult adminRegister(@Valid @RequestBody AdminRegisterRequest request) {
        return adminService.adminRegister(request);
    }

    /**
     * 修改密码
     *
     * @param request 修改密码请求
     * @return 修改密码结果
     */
    @SaCheckRole(value = "admin")
    @PostMapping("/change/pwd")
    public SaResult changePwd(@RequestBody ChangePwdRequest request) {
        return adminService.changePwd(request);
    }

    /**
     * 管理员登出
     *
     * @return 登出结果
     */
    @SaIgnore
    @GetMapping("/logout")
    public SaResult adminLogout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return SaResult.ok("登出成功！");
        }

        return SaResult.error("未登陆!");
    }

    /**
     * 获取登录角色
     *
     * @return 登录角色
     */
    @SaCheckRole("admin")
    @GetMapping("/role")
    public List<String> getLoginRole() {
        return adminService.getRole();
    }

    /**
     * 获取登录权限
     *
     * @return 登录权限
     */
    @SaCheckRole("admin")
    @GetMapping("/permission")
    public List<String> getLoginPermission() {
        return adminService.getPermission();
    }

    /**
     * 更改用户信息
     *
     * @param mode    查询模式
     * @param request 查询请求
     * @return 用户列表
     */
    @SaCheckPermission(value = {"change", "add"}, mode = SaMode.AND)
    @PostMapping("/changeUser/{mode}")
    public SaResult changeUserInfo(@PathVariable String mode, @RequestBody UpdateUserRequest request) {
        return adminService.changeUserInfo(mode, request);
    }

    /**
     * 添加广告
     *
     * @param request 添加广告请求
     * @return 添加状态
     */
    @SaCheckPermission("add")
    @PostMapping("/advertisements/add")
    public SaResult addAdvertisement(@RequestBody AddAdvertisementRequest request) {
        return adminService.addAdvertisement(request);
    }

    /**
     * 更新广告
     *
     * @param request 更新广告请求
     * @return 更新状态
     */
    @SaCheckPermission("change")
    @PostMapping("/advertisements/update")
    public SaResult updateAdvertisement(@Valid @RequestBody Advertisements request) {
        return adminService.updateAdvertisement(request);
    }

    /**
     * 获取广告
     *
     * @return 广告
     */
    @SaCheckPermission("read")
    @GetMapping("/advertisements/show")
    public SaResult getAdvertisement(@RequestParam @NotBlank(message = "页码不能为空") int page,
                                     @RequestParam @NotBlank(message = "大小不能为空") int size) {
        return adminService.getAdvertisement(page, size);
    }

    /**
     * 获取广告（搜索全局或有效）
     *
     * @param title 标题
     * @param mode  模式
     * @param page  页码
     * @param size  大小
     * @return 广告信息
     */
    @SaCheckPermission("read")
    @GetMapping("/advertisements/get")
    public SaResult getAdvertisement(
            @RequestParam @NotBlank(message = "标题不能为空") String title,
            @RequestParam String mode,
            @RequestParam int page,
            @RequestParam int size) {
        return adminService.getAdvertisement(title, mode, page, size);
    }

    /**
     * 删除广告
     *
     * @param id 广告ID
     * @return 删除状态
     */
    @SaCheckPermission("read")
    @PostMapping("/advertisements/delete/{id}")
    public SaResult deleteAdvertisement(@PathVariable String id) {
        return adminService.deleteAdvertisement(id);
    }

}
