package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.common.BaseContext;
import org.example.mymallapplication.dal.config.RabbitMQConfig;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.entity.product.ProductOrder;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.entity.product.UserOrder;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.product.*;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengyiyang
 */
@Slf4j
@Service
public class TableServiceImpl implements TableService {
    private static final String ORDER_DELAY_QUEUE = "order:delay:queue";
    @Autowired
    IProductsService productsService;
    @Autowired
    IFrontendUsersService usersService;
    @Autowired
    IOrdersService ordersService;
    @Autowired
    IUserOrderService userOrderService;
    @Autowired
    IProductOrderService productOrderService;
    @Autowired
    IAdminOrderService adminOrderService;
    @Autowired
    IBalanceService balanceService;
    @Autowired
    RedisService redis;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * <p>获取产品列表</p>
     *
     * @param name 产品名
     * @return 产品信息
     */
    @Override
    public SaResult getProduct(String name) {
        List<Products> products = productsService.getProductsLike(name);

        if (products.isEmpty()) {
            return SaResult.error("未找到！");
        }

        return SaResult.ok("查找成功！").setData(products);
    }

    /**
     * <p>购买商品</p>
     *
     * @param request 购买请求
     * @return 购买状态
     */
    @Override
    public SaResult buyProduct(@NotNull BuyProductRequest request) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        if (!productsService.hasProduct(request.getProductId())) {
            BaseContext.clear();
            return SaResult.error("不存在商品ID");
        }
        Products product = productsService.getProducts(request.getProductId());
        if (product.getNumber() < request.getNumber()) {
            BaseContext.clear();
            return SaResult.error("超量购买!");
        }

        String userId = StpUtil.getLoginIdAsString();
        Balance balance = balanceService.getBalanceByUserId(userId);
        double orgBalance = balance.getBalance();

        if (balance.getBalance() < product.getPrice() * request.getNumber()) {
            BaseContext.clear();
            return SaResult.error("余额不足！");
        }

        balance.setBalance(orgBalance - product.getPrice() * request.getNumber());
        balanceService.updateById(balance);
        product.setNumber(product.getNumber() - request.getNumber());
        if (!productsService.updateById(product)) {
            balance.setBalance(orgBalance);
            balanceService.updateById(balance);
            BaseContext.clear();
            return SaResult.error("购买失败！");
        }

        FrontendUsers user = usersService.getFrontendUsers(userId);
        Orders order = new Orders();
        order.setAddress(user.getAddress());
        order.setRemark(request.getRemark());
        order.setPrice(request.getNumber() * product.getPrice());
        order.setShippingTime(LocalDateTime.now());
        order.setState(State.valueOf("UNPAID"));
        order.setTime(LocalDateTime.now());
        order.setNumber(request.getNumber());

        if (!ordersService.save(order)) {
            balance.setBalance(orgBalance);
            balanceService.updateById(balance);
            BaseContext.clear();
            return SaResult.error("购买失败!");
        }

        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(request.getProductId());
        productOrder.setOrderId(order.getId());

        if (!productOrderService.save(productOrder)) {
            ordersService.removeById(order);
            balance.setBalance(orgBalance);
            balanceService.updateById(balance);
            BaseContext.clear();
            return SaResult.error(("购买失败！"));
        }

        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);
        userOrder.setOrderId(order.getId());

        if (!userOrderService.save(userOrder)) {
            productOrderService.removeById(productOrder);
            ordersService.removeById(order);
            balance.setBalance(orgBalance);
            balanceService.updateById(balance);
            BaseContext.clear();
            return SaResult.error("购买失败！");
        }

        Random random = new Random();
        int point = random.nextInt(50) + 80 + user.getPoint();
        user.setPoint(point);
        if (!usersService.updateById(user)) {
            BaseContext.clear();
            return SaResult.ok("购买成功！但加积分失败");
        }

        //添加到延迟队列
        /*
        long delayTime = System.currentTimeMillis() + 5 * 60 * 1000;
        redis.delayQueue(ORDER_DELAY_QUEUE, String.valueOf(order.getId()), delayTime);

        AdminOrder adminOrder = new AdminOrder();
        adminOrder.setAdminId(1L);
        adminOrder.setOrderId(order.getId());
        if (!adminOrderService.save(adminOrder)) {
            log.error("映射到管理员失败，请管理员手动映射数据库！");
            log.info("订单ID：" + order.getId().toString());
        }
         */
        long delay = 30 * 1000;
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE_NAME, RabbitMQConfig.ORDER_CANCEL_ROUTING_KEY,
                order, message -> {
                    message.getMessageProperties().getHeaders().putAll(new HashMap<>() {{
                        put("x-delay", delay);
                    }});
                    return message;
                });

        BaseContext.clear();
        return SaResult.ok("下单成功！");
    }

    /**
     * 下单商品列表
     *
     * @param requests 商品列表
     * @return 购买状态
     */
    @Override
    public SaResult buyProductByIds(@NotNull List<BuyProductRequest> requests) {
        String userId = StpUtil.getLoginIdAsString();
        BaseContext.setCurrentId(userId);

        //<productId,request>
        Map<String, BuyProductRequest> buyMap = requests.stream()
                .collect(Collectors.toMap(BuyProductRequest::getProductId, p -> p));
        List<String> productIds = requests.stream().map(BuyProductRequest::getProductId).toList();
        List<String> errList = new ArrayList<>(productIds);
        try {
            List<String> existIds = productsService.getExistingProductIds(productIds);
            errList.removeAll(existIds);
            if (!errList.isEmpty()) {
                throw new Exception("有商品不存在");
            }

            List<Products> products = productsService.getProducts(productIds);
            Map<String, String> orderIds = new HashMap<>();
            for (Products product : products) {
                Orders order = new Orders();
                order.setState(State.UNPAID);
                order.setTime(LocalDateTime.now());
                int number = buyMap.get(product.getId()).getNumber();
                order.setNumber(number);
                order.setPrice(product.getPrice() * number);
                order.setRemark(buyMap.get(product.getId()).getRemark());
                ordersService.save(order);
                Thread.sleep(50);
                rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
                        order.getId(), message -> {
                            message.getMessageProperties().getHeaders().putAll(new HashMap<>() {{
                                put("x-delay", 5 * 60 * 1000);
                            }});
                            return message;
                        }
                );
                orderIds.put(product.getId(), order.getId());
            }
            List<ProductOrder> productOrders = new ArrayList<>();
            for (String productId : productIds) {
                ProductOrder productOrder = new ProductOrder();
                productOrder.setProductId(productId);
                productOrder.setOrderId(orderIds.get(productId));
                productOrders.add(productOrder);
            }
            productOrderService.saveBatch(productOrders);
            List<UserOrder> userOrders = new ArrayList<>();
            for (String orderId : orderIds.keySet()) {
                UserOrder userOrder = new UserOrder();
                userOrder.setOrderId(orderId);
                userOrder.setUserId(userId);
                userOrders.add(userOrder);
            }
            userOrderService.saveBatch(userOrders);

        } catch (Exception e) {
            log.error("查找商品失败: {}", e.getMessage());
            BaseContext.clear();
            return SaResult.error(e.getMessage()).setData(errList);
        }

        BaseContext.clear();
        return SaResult.ok("success");
    }

    /**
     * <p>取消订单服务</p>
     *
     * @param request 取消请求
     * @return 取消状态
     */
    @Override
    public SaResult orderCancel(@NotNull OrderCancelRequest request) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        Orders order = ordersService.getById(request.getOrderId());
        order.setState(State.valueOf("CANCEL"));
        String productId = productOrderService.getProductId(order.getId());
        Products product = productsService.getProducts(productId);
        product.setNumber(product.getNumber() + order.getNumber());

        if (!ordersService.updateById(order) || !productsService.updateById(product)) {
            BaseContext.clear();
            return SaResult.error("取消失败！");
        }
        BaseContext.clear();
        return SaResult.ok("取消成功!");
    }

    /**
     * <p>获取登陆用户的订单信息</p>
     *
     * @return 订单信息
     */
    @Override
    public SaResult getMyOrder() {
        String userId = StpUtil.getLoginIdAsString();
        List<String> orderId = userOrderService.getOrderId(userId);
        if (orderId.isEmpty()) {
            return SaResult.error("无订单信息！");
        }
        List<Orders> orders = ordersService.getOrders(orderId);

        return SaResult.ok("获取订单信息成功!").setData(orders);
    }

    /**
     * 15天自动收货
     *
     * @param orderIds 订单ID列表
     */
    @Override
    @Async
    public void toFifteenDaysQueue(@NotNull List<String> orderIds) {
        long delay = 15 * 24 * 60 * 60 * 1000;
//        long delay = 30 * 1000;

        orderIds.forEach(orderId -> {
            Map<String, Object> headers = new HashMap<>();
            headers.put("x-delay", delay);

            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, orderId,
                    message -> {
                        message.getMessageProperties().getHeaders().putAll(headers);
                        return message;
                    });

            log.info("Order " + orderId + " shipped. Auto-confirm in 15 days.");
        });

    }

}
