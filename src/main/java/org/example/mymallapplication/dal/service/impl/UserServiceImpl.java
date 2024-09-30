package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.common.BaseContext;
import org.example.mymallapplication.dal.config.RabbitMQConfig;
import org.example.mymallapplication.dal.dao.entity.commit.ProductReviews;
import org.example.mymallapplication.dal.dao.entity.commit.ReviewLikes;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.service.commit.IProductReviewsService;
import org.example.mymallapplication.dal.dao.service.commit.IReviewLikesService;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.*;
import org.example.mymallapplication.dal.vo.response.UserLoginResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chengyiyang
 */
@Slf4j
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
    private IProductReviewsService productReviewsService;
    @Autowired
    private IReviewLikesService reviewLikesService;

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
        BeanUtil.copyProperties(request, users);

        users.setPassword(SaSecureUtil.sha256(request.getPassword()));
        try {
            usersService.save(users);

            Balance balance = new Balance();
            balance.setUserId(users.getId());
            balanceService.save(balance);
        } catch (Exception e) {
            log.error("用户注册数据库错误: {}", e.toString());
            return SaResult.error("用户数据库错误");
        }

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
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        String userId = StpUtil.getLoginIdAsString();
        FrontendUsers user = usersService.getFrontendUsers(userId);
        if (redis.hasKey(user.getUsername())) {
            redis.deleteKey(user.getUsername());
        }

        user.setPassword(SaSecureUtil.sha256(request.getPassword()));
        if (!usersService.updateById(user)) {
            BaseContext.clear();
            return SaResult.error("改密失败，请重新更改！");
        }

        StpUtil.logout();
        BaseContext.clear();
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
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        //查询是否到账(Api待补全)

        //更新数据库
        String userId = StpUtil.getLoginIdAsString();
        Balance balance = balanceService.getBalanceByUserId(userId);
        balance.setBalance(balance.getBalance() + money);
        balanceService.updateById(balance);

        BaseContext.clear();
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
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        //查询是否到账(Api待补全)

        //更新数据库
        String userId = StpUtil.getLoginIdAsString();
        Balance balance = balanceService.getBalanceByUserId(userId);
        if (balance.getBalance() < money) {
            return SaResult.error("余额不足！");
        }
        balance.setBalance(balance.getBalance() - money);
        balanceService.updateById(balance);

        BaseContext.clear();
        return SaResult.ok("提现成功！");
    }

    /**
     * <p>获取用户实体类</p>
     *
     * @return 用户实体类
     */
    @Override
    public SaResult getUserInfo() {
        String username = String.valueOf(StpUtil.getSession().get("username"));
        if (redis.hasKey(username)) {
            FrontendUsers users = redis.getUserFromRedis(username);
            return SaResult.ok("success").setData(users);
        }

        FrontendUsers users = usersService.getFrontendUsers(StpUtil.getLoginIdAsString());
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
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        try {
            if (usersService.updateAddress(StpUtil.getLoginIdAsString(), address)) {
                BaseContext.clear();
                return SaResult.ok("success");
            } else {
                throw new Exception("数据库操作错误");
            }
        } catch (Exception e) {
            log.error("设置地址数据库错误: {}", e.toString());
            BaseContext.clear();
            return SaResult.error("服务器错误" + e.toString());
        }
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
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_CONFIRM_EXCHANGE_NAME,
                    RabbitMQConfig.ORDER_CONFIRM_ROUTING_KEY, order, message -> {
                        message.getMessageProperties().getHeaders().putAll(new HashMap<>() {{
                            put("handle-confirm", request.getOrderId());
                        }});
                        return message;
                    });
        } catch (Exception e) {
            log.error("添加到延迟队列失败: {}", e.toString());
            return SaResult.error("添加到延迟队列失败:" + e.toString());
        }

        return SaResult.ok("确认收货成功！");
    }

    /**
     * 写评论
     *
     * @param request 评论请求
     * @return 评论状态
     */
    @Override
    public SaResult writeCommit(WriteCommitRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        ProductReviews reviews = new ProductReviews();
        BeanUtil.copyProperties(request, reviews);
        reviews.setUserId(userId);
        try {
            productReviewsService.save(reviews);
        } catch (Exception e) {
            log.error("评论保存失败: {}", e.toString());
            BaseContext.clear();
            return SaResult.error("评论保存失败: " + e.toString());
        }

        BaseContext.clear();
        return SaResult.ok("success");
    }

    /**
     * 回复评论
     *
     * @param request 请求
     * @return 状态
     */
    @Override
    public SaResult replyCommit(CommitReplyRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        ProductReviews reviews = new ProductReviews();
        BeanUtil.copyProperties(request, reviews);

        reviews.setUserId(userId);
        ProductReviews parentReviews = productReviewsService.getById(reviews.getParentId());
        if (parentReviews != null) {
            reviews.setParentId(parentReviews.getParentId());
        }

        try {
            productReviewsService.save(reviews);
        } catch (Exception e) {
            BaseContext.clear();
            return SaResult.error("数据库错误" + e.toString());
        }

        BaseContext.clear();
        return SaResult.ok("success");
    }

    /**
     * 点赞评论
     *
     * @param id 评论ID
     * @return 状态
     */
    @Override
    public SaResult likeCommit(String id) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        ReviewLikes reviewLikes = new ReviewLikes();
        reviewLikes.setUserId(userId);
        reviewLikes.setReviewId(id);

        try {
            reviewLikesService.save(reviewLikes);
            ProductReviews reviews = productReviewsService.getReview(id);
            reviews.setLikeCount(reviews.getLikeCount() + 1);
            productReviewsService.save(reviews);
        } catch (Exception e) {
            log.error("点赞保存错误: {}", e.toString());
            BaseContext.clear();
            return SaResult.error("点赞保存错误" + e.toString());
        }

        BaseContext.clear();
        return SaResult.ok("success");
    }

    /**
     * 获取评论
     *
     * @param productId 商品ID
     * @return 评论
     */
    @Override
    public SaResult getCommit(String productId, int page, int size) {
        IPage<ProductReviews> mainReviews = productReviewsService.getMainReviews(productId, page, size);

        List<String> mainReviewIds = mainReviews.getRecords().stream()
                .map(ProductReviews::getId).toList();
        Map<String, List<ProductReviews>> replyMap = productReviewsService.getRepliesByParentIds(mainReviewIds);

        List<NewReviews> newReviews = new ArrayList<>();
        for (ProductReviews review : mainReviews.getRecords()) {
            NewReviews newReview = new NewReviews();
            newReview.setReviews(replyMap.getOrDefault(review.getParentId(), new ArrayList<>()));
            newReviews.add(newReview);
        }

        return SaResult.ok("success")
                .setData(newReviews)
                .set("currentPage", mainReviews.getCurrent())
                .set("totalPages", mainReviews.getPages())
                .set("totalRecords", mainReviews.getTotal());
    }
}

@Getter
@Setter
class NewReviews extends ProductReviews {
    private List<ProductReviews> reviews = new ArrayList<>();
}
