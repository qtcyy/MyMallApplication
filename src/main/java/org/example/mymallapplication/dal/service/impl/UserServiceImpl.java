package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;
import org.example.mymallapplication.dal.vo.response.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisService redis;
    @Autowired
    private IFrontendUsersService usersService;
    @Autowired
    private IUsersService adminService;

    /**
     * <p>用户登陆服务</p>
     *
     * @param request 登陆请求信息
     * @return 登陆结果信息
     */
    @Override
    public SaResult userLogin(UserLoginRequest request) {
        if (!usersService.hasUser(request.getUsername())) {
            return SaResult.error("用户名或密码错误！");
        }
        FrontendUsers users = usersService.getFrontendUsers(request.getUsername());
        if (SaSecureUtil.sha256(request.getPassword()).equals(users.getPassword())) {
            StpUtil.login(users.getId());
            StpUtil.getSession().set("username", users.getUsername());
            redis.saveUserToRedis(request.getUsername(), users);
            redis.setExpire(request.getUsername(), 1L, TimeUnit.DAYS);

            return SaResult.ok("登陆成功!")
                    .setData(new UserLoginResponse(StpUtil.getTokenValue()));
        }

        return SaResult.error("用户名或密码错误!");
    }

    /**
     * <p>用户注册服务</p>
     *
     * @param request 注册信息请求
     * @return 注册状态
     */
    @Override
    public SaResult userRegister(UserRegisterRequest request) {
        if (usersService.hasUser(request.getUsername()) || adminService.hasUser(request.getUsername())) {
            return SaResult.error("用户名已存在！");
        }
        FrontendUsers users = new FrontendUsers();
        users.setUsername(request.getUsername());
        users.setPassword(SaSecureUtil.sha256(request.getPassword()));
        users.setNickname(request.getNickname());
        users.setLocation(request.getLocation());
        users.setPhone(request.getPhone());
        users.setEmail(request.getEmail());
        users.setGender(request.getGender());

        if (usersService.save(users)) {
            return SaResult.ok("注册成功！");
        }

        return SaResult.error("注册失败！");
    }

    /**
     * <p>获取用户实体类</p>
     *
     * @return 用户实体类
     */
    @Override
    public SaResult getUserInfo() {
        String username = (String) StpUtil.getSession().get("username");
        if (redis.hasKey(username)) {
            FrontendUsers users = redis.getUserFromRedis(username);
            return SaResult.ok("success").setData(users);
        }

        FrontendUsers users = usersService.getFrontendUsers((Long) StpUtil.getLoginId());
        redis.saveUserToRedis(username, users);

        return SaResult.ok("success").setData(users);
    }

    /**
     * <p>设置地址</p>
     *
     * @param address 地址
     * @return 更新状态
     */
    @Override
    public SaResult setAddress(String address) {
        if (usersService.updateAddress((Long) StpUtil.getLoginId(), address)) {
            return SaResult.ok("success");
        }

        return SaResult.error("服务器错误");
    }
}
