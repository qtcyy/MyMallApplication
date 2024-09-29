package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.config.RabbitMQConfig;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.ChangePwdRequest;
import org.example.mymallapplication.dal.vo.request.ConfirmOrderRequest;
import org.example.mymallapplication.dal.vo.request.UserLoginRequest;
import org.example.mymallapplication.dal.vo.request.UserRegisterRequest;
import org.example.mymallapplication.dal.vo.response.UserLoginResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisService redis;
    @Autowired
    private IFrontendUsersService usersService;
    @Autowired
    private IUsersService adminService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

        usersService.save(users);


        Balance balance = new Balance();
        balance.setUserId(users.getId());
        balanceService.save(balance);

        return SaResult.ok("注册成功！");
    }

    /**
     * <p>用户改密服务</p>
     *
     * @param request 改密请求
     * @return 改密状态
     */
    @Override
    public SaResult changePwd(ChangePwdRequest request) {
        String userId = (String) StpUtil.getLoginId();
        FrontendUsers user = usersService.getFrontendUsers(userId);
        if (redis.hasKey(user.getUsername())) {
            redis.deleteKey(user.getUsername());
        }

        user.setPassword(SaSecureUtil.sha256(request.getPassword()));
        if (!usersService.updateById(user)) {
            return SaResult.error("改密失败，请重新更改！");
        }

        StpUtil.logout();
        return SaResult.ok("改密成功，请重新登陆！");
    }

    /**
     * 用户充值
     *
     * @param money 金额
     * @return 充值状态
     */
    @Override
    public SaResult rechargeBalance(double money) {
        //查询是否到账(Api待补全)

        //更新数据库
        String userId = (String) StpUtil.getLoginId();
        Balance balance = balanceService.getBalanceByUserId(userId);
        balance.setBalance(balance.getBalance() + money);
        balanceService.updateById(balance);

        return SaResult.ok("充值成功！");
    }

    /**
     * 提现
     *
     * @param money 金额
     * @return 提现状态
     */
    @Override
    public SaResult withdrawBalance(double money) {
        //查询是否到账(Api待补全)

        //更新数据库
        String userId = (String) StpUtil.getLoginId();
        Balance balance = balanceService.getBalanceByUserId(userId);
        if (balance.getBalance() < money) {
            return SaResult.error("余额不足！");
        }
        balance.setBalance(balance.getBalance() - money);
        balanceService.updateById(balance);

        return SaResult.ok("提现成功！");
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

        FrontendUsers users = usersService.getFrontendUsers((String) StpUtil.getLoginId());
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
        if (usersService.updateAddress((String) StpUtil.getLoginId(), address)) {
            return SaResult.ok("success");
        }

        return SaResult.error("服务器错误");
    }

    /**
     * <p>确认收获服务</p>
     *
     * @param request 确认请求
     * @return 确认状态
     */
    @Override
    public SaResult confirmOrder(ConfirmOrderRequest request) {
        Orders order = ordersService.getById(request.getOrderId());
        if (order.getState().equals(State.END) || order.getState().equals(State.DELETED)) {
            return SaResult.error("不允许次操作");
        }
        if (order.getState().equals(State.PAID)) {
            return SaResult.error("还未发货！");
        }
        order.setState(State.FINISH);
//        ordersService.updateById(order);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_CONFIRM_EXCHANGE_NAME,
                RabbitMQConfig.ORDER_CONFIRM_ROUTING_KEY, order, message -> {
                    message.getMessageProperties().getHeaders().putAll(new HashMap<>() {{
                        put("handle-confirm", request.getOrderId());
                    }});
                    return message;
                });

        return SaResult.ok("确认收货成功！");
    }
}
