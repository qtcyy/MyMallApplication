package org.example.mymallapplication.dal.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.config.RabbitMQConfig;
import org.example.mymallapplication.dal.dao.entity.person.AdminBalance;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.entity.product.AdminOrder;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.service.person.IAdminBalanceService;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.example.mymallapplication.dal.dao.service.product.*;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.service.ScheduledService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduledServiceImpl implements ScheduledService {
    private static final String ORDER_DELAY_QUEUE = "order:delay:queue";
    private final RateLimiter rateLimiter = RateLimiter.create(100.0);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    IOrdersService ordersService;
    @Autowired
    IUserOrderService userOrderService;
    @Autowired
    IProductOrderService productOrderService;
    @Autowired
    IProductsService productsService;
    @Autowired
    IBalanceService balanceService;
    @Autowired
    IAdminOrderService adminOrderService;
    @Autowired
    IAdminBalanceService adminBalanceService;

    /**
     * <p>处理延时队列（每秒一次）</p>
     */
    @Override
    public void processDelayedOrders() {
        long currentTime = System.currentTimeMillis();

        //从redis ZSet中取出需要处理的订单
        Set<String> orderIds = Objects.requireNonNull(redisTemplate.opsForZSet()
                        .rangeByScore(ORDER_DELAY_QUEUE, 0, currentTime)).stream()
                .map(String::valueOf).collect(Collectors.toSet());

        if (!orderIds.isEmpty()) {
            for (String orderId : orderIds) {
                Orders order = ordersService.getById(Long.valueOf(orderId));

                if (State.UNPAID.equals(order.getState())) {
                    order.setState(State.DELETED);
                    //退款
                    ordersService.updateById(order);
                    String userId = userOrderService.getUserId(orderId);
                    Balance balance = balanceService.getBalanceByUserId(userId);
                    balance.setBalance(balance.getBalance() + order.getPrice());
                    balanceService.updateById(balance);

                    //返回库存
                    String productId = productOrderService.getProductId(orderId);
                    Products product = productsService.getProducts(productId);
                    product.setNumber(product.getNumber() + order.getNumber());
                    productsService.saveProduct(product);
                }

                //从redis中删除订单
                redisTemplate.opsForZSet().remove(ORDER_DELAY_QUEUE, orderId);
            }
        }
    }

    @Override
    public void processLandingOrders() {
        List<Orders> orders = ordersService.getOrderByState(State.FINISH);
        if (orders.isEmpty()) {
            return;
        }

        List<String> orderIds = new ArrayList<>();
        orders.forEach(order -> {
            orderIds.add(order.getId());
            order.setState(State.END);
        });
        ordersService.updateBatchById(orders);

        Map<String, String> orderAdminIdMap = new HashMap<>();
        List<AdminOrder> adminOrders = adminOrderService.getAdminIds(orderIds);
        List<String> adminIds = new ArrayList<>();
        adminOrders.forEach(obj -> {
            adminIds.add(obj.getAdminId());
            orderAdminIdMap.put(obj.getOrderId(), obj.getAdminId());
        });

        List<Balance> balances = balanceService.getBalance(adminIds);
        Map<String, Balance> adminBalance = new HashMap<>();
        balances.forEach(balance -> {
            adminBalance.put(balance.getUserId(), balance);
        });

        orders.forEach(order -> {
            String adminId = orderAdminIdMap.get(order.getId());
            Balance balance = adminBalance.get(adminId);
            balance.setBalance(balance.getBalance() + order.getPrice());
            adminBalance.put(adminId, balance);
        });
        List<Balance> newBalances = new ArrayList<>();
        adminBalance.forEach((key, val) -> {
            newBalances.add(val);
        });

        balanceService.updateBatchById(newBalances);
    }

    /**
     * 监听确认收货队列并处理(包含手动确认和自动确认)
     *
     * @param order 接收队列中的订单
     */
    @Override
    @RabbitListener(queues = {RabbitMQConfig.ORDER_CONFIRM_QUEUE_NAME, RabbitMQConfig.DELAYED_QUEUE_NAME})
    @Async
    public void processOrderConfirm(Orders order, @Header(value = "handle-confirm", required = false) String headerKey) {
        rateLimiter.acquire();
        if (headerKey.equals("handle-confirm")) {
            log.info("handle-confirm order: {}", order.getId());
        } else if (headerKey.equals("x-delay")) {
            log.info("auto-confirm order: {}", order.getId());
        }

        String orderId = order.getId();
        String adminId = adminOrderService.getAdminId(orderId);
        AdminBalance balance = adminBalanceService.getAdminBalance(adminId);
        balance.setBalance(balance.getBalance() + order.getPrice());
        try {
            if (!order.getState().equals(State.END)) {
                order.setState(State.END);
                ordersService.updateById(order);
            }
            adminBalanceService.updateById(balance);
        } catch (Exception e) {
            log.error("确认收货数据库处理错误: {}", e.toString());
        }
    }

    /**
     * 取消过期的订单
     *
     * @param order 订单
     */
    @Override
    @Async
    @RabbitListener(queues = RabbitMQConfig.DELAYED_CANCEL_QUEUE_NAME)
    public void processOrderDelayedCancel(Orders order) {
        rateLimiter.acquire();

        Orders newOrder = ordersService.getById(order.getId());
        if (newOrder.getState().equals(State.UNPAID)) {
            order.setState(State.DELETED);
            ordersService.updateById(order);
            String userId = userOrderService.getUserId(order.getId());
            Balance balance = balanceService.getBalanceByUserId(userId);
            balance.setBalance(balance.getBalance() + order.getPrice());

            try {
                balanceService.updateById(balance);
                String productId = productOrderService.getProductId(order.getId());
                Products product = productsService.getProducts(productId);
                product.setNumber(product.getNumber() + order.getNumber());
                productsService.updateById(product);
            } catch (Exception e) {
                log.error("取消订单数据库处理错误: {}", e.toString());
            }
        }
    }
}
