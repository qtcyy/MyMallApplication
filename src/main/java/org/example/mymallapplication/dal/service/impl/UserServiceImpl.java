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
import org.example.mymallapplication.dal.dao.entity.commit.ReviewImages;
import org.example.mymallapplication.dal.dao.entity.commit.ReviewLikes;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.entity.person.Cart;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.product.Advertisements;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.service.commit.IProductReviewsService;
import org.example.mymallapplication.dal.dao.service.commit.IReviewImagesService;
import org.example.mymallapplication.dal.dao.service.commit.IReviewLikesService;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.example.mymallapplication.dal.dao.service.person.ICartService;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.person.IUsersService;
import org.example.mymallapplication.dal.dao.service.product.IAdvertisementsService;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.example.mymallapplication.dal.dao.service.product.IUserOrderService;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.*;
import org.example.mymallapplication.dal.vo.response.GetCartResponse;
import org.example.mymallapplication.dal.vo.response.UserLoginResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private ICartService cartService;
    @Autowired
    private IProductsService productsService;
    @Autowired
    private IUserOrderService userOrderService;
    @Autowired
    private IAdvertisementsService advertisementsService;
    @Autowired
    private IReviewImagesService reviewImagesService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * <p>用户登陆服务</p>
     *
     * @param request 登陆请求信息
     * @return 登陆结果信息
     */
    @Override
    public SaResult userLogin(@NotNull UserLoginRequest request) {
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
    public SaResult userRegister(@NotNull UserRegisterRequest request) {
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
    public SaResult confirmOrder(@NotNull ConfirmOrderRequest request) {
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
     * @return 评论状态(包含评论ID " reviewId ")
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
        return SaResult.ok("success").set("reviewId", reviews.getId());
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

    /**
     * 获取评图片
     *
     * @param reviewId 评论ID
     * @return 评论图片
     */
    @Override
    public SaResult getCommitImages(String reviewId) {
        List<ReviewImages> reviewImages = reviewImagesService.getReviewImages(reviewId);
        return SaResult.ok("success").setData(reviewImages);
    }

    /**
     * 添加到购物车
     *
     * @param request 信息
     * @return 添加信息
     */
    @Override
    public SaResult addToCart(@org.jetbrains.annotations.NotNull AddCartRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        Cart cart = cartService.getCart(userId, request.getProductId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(request.getProductId());
            cart.setQuantity(request.getQuantity());
            try {
                cartService.save(cart);
            } catch (Exception e) {
                log.error("添加到数据库错误: {}", e.toString());
                BaseContext.clear();
                return SaResult.error("添加到数据库错误:" + e.toString());
            }
        } else {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
            try {
                cartService.updateById(cart);
            } catch (Exception e) {
                log.error("更新到数据库错误: {}", e.toString());
                BaseContext.clear();
                return SaResult.error("更新到数据库错误:" + e.toString());
            }
        }

        BaseContext.clear();
        return SaResult.ok("success");
    }

    /**
     * 获取购物车信息
     *
     * @return 购物车信息
     */
    @Override
    public SaResult getCart() {
        String userId = StpUtil.getLoginIdAsString();
        List<GetCartResponse> responses = new ArrayList<>();
        List<Cart> userCart = cartService.getCart(userId);
        List<String> productIds = userCart.stream().map(Cart::getProductId).toList();

        //<商品ID, 商品>
        Map<String, Products> productsMap = productsService.getProductsIdMap(productIds);
        for (Cart cart : userCart) {
            Products products = productsMap.getOrDefault(cart.getProductId(), new Products());
            GetCartResponse getCartResponse = new GetCartResponse();
            BeanUtil.copyProperties(products, getCartResponse);
            getCartResponse.setQuantity(cart.getQuantity());
            responses.add(getCartResponse);
        }

        return SaResult.ok("success").setData(responses);
    }

    /**
     * 获取未支付的订单
     *
     * @return 订单信息
     */
    @Override
    public SaResult getUnpaidOrder() {
        String userId = StpUtil.getLoginIdAsString();

        try {
            List<String> orderIds = userOrderService.getOrderId(userId);
            List<Orders> orders = ordersService.getOrdersWithState(orderIds, State.UNPAID);
            double price = 0.0;
            for (Orders order : orders) {
                price += order.getPrice();
            }
            return SaResult.ok("success").setData(orders).set("price", price);
        } catch (Exception e) {
            log.error("获取未支付订单错误: {}", e.toString());
            return SaResult.error("获取未支付订单错误: " + e.toString());
        }
    }

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @return 状态
     */
    @Override
    public SaResult payOrder(String orderId) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);
        Orders orders = ordersService.getById(orderId);
        //获取支付状态(Api 待实现 当前默认支付成功)

        orders.setState(State.PAID);
        try {
            ordersService.updateById(orders);
            BaseContext.clear();
            return SaResult.ok("success");
        } catch (Exception e) {
            log.error("数据库错误: {}", e.toString());
            BaseContext.clear();
            return SaResult.error("数据库错误: " + e.toString()).setData(orders);
        }
    }

    /**
     * 批量支付订单
     *
     * @param orderIds 订单ID列表
     * @return 支付状态
     */
    @Override
    public SaResult payOrders(List<String> orderIds) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        List<Orders> orders = ordersService.getOrders(orderIds);
        double price = 0.0;
        for (Orders order : orders) {
            price += order.getPrice();
        }
        //检查支付状态(APi 待实现 默认支付成功)

        for (Orders order : orders) {
            order.setState(State.PAID);
        }
        try {
            ordersService.updateBatchById(orders);
            return SaResult.ok("success").set("price", price);
        } catch (Exception e) {
            log.error("更新数据库错误: {}", e.toString());
            BaseContext.clear();
            return SaResult.error("更新数据库错误: " + e.toString());
        }
    }

    /**
     * 显示广告
     *
     * @return 广告信息
     */
    @Override
    public SaResult showAdvertisement() {
        try {
            List<Advertisements> list = advertisementsService.getAvailableAdvertisements();

            return SaResult.ok("success").setData(list);
        } catch (Exception e) {
            log.error("获取广告错误: {}", e.toString());
            return SaResult.error("获取广告错误: " + e.toString());
        }
    }

    /**
     * 添加评论图片
     *
     * @return 添加状况
     */
    @Override
    public SaResult addReviewImage(AddImageRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        ReviewImages reviewImage = new ReviewImages();
        BeanUtil.copyProperties(request, reviewImage);

        SaResult result;
        try {
            reviewImagesService.save(reviewImage);
            result = SaResult.ok("success");
        } catch (Exception e) {
            log.error("添加图片错误: {}", e.toString());
            result = SaResult.error("添加图片错误: " + e.toString());
        } finally {
            BaseContext.clear();
        }

        return result;
    }
}

@Getter
@Setter
class NewReviews extends ProductReviews {
    private List<ProductReviews> reviews = new ArrayList<>();
}
