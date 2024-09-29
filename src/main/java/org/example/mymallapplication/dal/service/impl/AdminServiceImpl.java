package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.common.BaseContext;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.person.Users;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.AdminService;
import org.example.mymallapplication.dal.service.DBService;
import org.example.mymallapplication.dal.vo.request.AdminLoginRequest;
import org.example.mymallapplication.dal.vo.request.AdminRegisterRequest;
import org.example.mymallapplication.dal.vo.request.ChangePwdRequest;
import org.example.mymallapplication.dal.vo.request.UpdateUserRequest;
import org.example.mymallapplication.dal.vo.response.AdminLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private IFrontendUsersService service;
    @Autowired
    private RedisService redis;
    @Autowired
    private DBService dbService;

    /**
     * <p>管理员登陆服务</p>
     *
     * @param request 登陆请求
     * @return 登陆状态信息
     */
    @Override
    public SaResult adminLogin(AdminLoginRequest request) {
        if (!usersService.hasUser(request.getUsername())) {
            return SaResult.error("用户名或密码错误！");
        }

        Users users = usersService.getUser(request.getUsername());
        if (users.getPassword().equals(SaSecureUtil.sha256(request.getPassword()))) {
            StpUtil.login(users.getId());
            StpUtil.getSession().set("username", users.getUsername());

            redis.saveAdminToRedis(request.getUsername(), users);
            redis.setExpire(users.getUsername(), 1L, TimeUnit.DAYS);

            return SaResult.ok("登陆成功！")
                    .setData(new AdminLoginResponse(StpUtil.getTokenValue()));
        }

        return SaResult.error("用户名或密码错误!");
    }

    /**
     * <p>管理员注册服务</p>
     *
     * @param request 注册请求信息
     * @return 注册状态
     */
    @Override
    public SaResult adminRegister(AdminRegisterRequest request) {
        if (usersService.hasUser(request.getUsername()) || service.hasUser(request.getUsername())) {
            return SaResult.error("用户已存在!");
        }

        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setPassword(SaSecureUtil.sha256(request.getPassword()));

        if (usersService.save(users)) {
            return SaResult.ok("注册成功!");
        }

        return SaResult.error("注册失败！");
    }

    /**
     * <p>更改管理员密码</p>
     *
     * @param request 改密请求
     * @return 改密状态
     */
    @Override
    public SaResult changePwd(ChangePwdRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);
        Users user = usersService.getById(userId);
        if (redis.hasKey(user.getUsername())) {
            redis.deleteKey(user.getUsername());
        }

        user.setPassword(SaSecureUtil.sha256(request.getPassword()));
        if (!usersService.updateById(user)) {
            return SaResult.error("更改密码失败！请重新更改");
        }

        StpUtil.logout();
        BaseContext.clear();
        return SaResult.ok("更改成功！请重新登陆");
    }

    /**
     * <p>获取登陆管理员的角色</p>
     *
     * @return 角色字符串列表
     */
    @Override
    public List<String> getRole() {
        String userId = StpUtil.getLoginIdAsString();
        if (!usersService.hasUser(userId)) {
            return Collections.emptyList();
        }
        return dbService.getRoles(userId);
    }

    /**
     * <p>获取登陆管理员的权限</p>
     *
     * @return 权限字符串列表
     */
    @Override
    public List<String> getPermission() {
        String userId = StpUtil.getLoginIdAsString();
        return dbService.getPermissions(userId);
    }

    /**
     * <p>更改用户信息</p>
     *
     * @param mode    更改模式
     * @param request 更改信息请求
     * @return 更改状态
     */
    @Override
    public SaResult changeUserInfo(String mode, UpdateUserRequest request) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        switch (mode) {
            case "insert": {
                if (service.hasUser(request.getUsername())) {
                    return SaResult.error("已存在");
                }
                FrontendUsers users = new FrontendUsers();
                BeanUtil.copyProperties(request, users, CopyOptions.create().setIgnoreNullValue(true));
                if (request.getPassword() != null)
                    users.setPassword(SaSecureUtil.sha256(request.getPassword()));

                try {
                    if (service.save(users)) {
                        BaseContext.clear();
                        return SaResult.ok("添加成功");
                    } else {
                        throw new Exception("数据库错误");
                    }
                } catch (Exception e) {
                    log.error("插入添加失败: {}", e.toString());
                    BaseContext.clear();
                    return SaResult.error("添加失败" + e.toString());
                }
            }
            case "update": {
                if (!service.hasUser(request.getUsername())) {
                    BaseContext.clear();
                    return SaResult.error("用户不存在");
                }

                FrontendUsers users = service.getFrontendUsers(request.getUsername());
                BeanUtil.copyProperties(request, users, CopyOptions.create().setIgnoreNullValue(true));
                if (request.getPassword() != null)
                    users.setPassword(SaSecureUtil.sha256(request.getPassword()));

                try {
                    if (service.updateById(users)) {
                        BaseContext.clear();
                        return SaResult.ok("修改成功");
                    } else {
                        throw new Exception("数据库更新失败");
                    }

                } catch (Exception e) {
                    log.error("更新添加失败: {}", e.toString());
                    BaseContext.clear();
                    return SaResult.error("修改失败!" + e.toString());
                }
            }

        }
        BaseContext.clear();
        return SaResult.error("类型错误");
    }
}
