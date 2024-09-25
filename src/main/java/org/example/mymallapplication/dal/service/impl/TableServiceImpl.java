package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.product.*;
import org.example.mymallapplication.dal.dao.service.person.IFrontendUsersService;
import org.example.mymallapplication.dal.dao.service.product.*;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class TableServiceImpl implements TableService {
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
    public SaResult buyProduct(BuyProductRequest request) {
        if (!productsService.hasProduct(request.getProductId())) {
            return SaResult.error("不存在商品ID");
        }
        Products product = productsService.getProducts(request.getProductId());
        if (product.getNumber() < request.getNumber()) {
            return SaResult.error("超量购买!");
        }

        product.setNumber(product.getNumber() - request.getNumber());
        if (!productsService.updateById(product)) {
            return SaResult.error("购买失败！");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        FrontendUsers user = usersService.getFrontendUsers(userId);
        Orders order = new Orders();
        order.setAddress(user.getAddress());
        order.setRemark(request.getRemark());
        order.setState(State.valueOf("UNPAID"));
        order.setTime(LocalDateTime.now());
        order.setNumber(request.getNumber());

        if (!ordersService.save(order)) {
            return SaResult.error("购买失败!");
        }

        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(request.getProductId());
        productOrder.setOrderId(order.getId());

        if (!productOrderService.save(productOrder)) {
            ordersService.removeById(order);
            return SaResult.error(("购买失败！"));
        }

        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);
        userOrder.setOrderId(order.getId());

        if (!userOrderService.save(userOrder)) {
            productOrderService.removeById(productOrder);
            ordersService.removeById(order);
            return SaResult.error("购买失败！");
        }

        Random random = new Random();
        int point = random.nextInt(50) + 80 + user.getPoint();
        user.setPoint(point);
        if (!usersService.updateById(user)) {
            return SaResult.ok("购买成功！但加积分失败");
        }

        AdminOrder adminOrder = new AdminOrder();
        adminOrder.setAdminId(1L);
        adminOrder.setOrderId(order.getId());
        if (!adminOrderService.save(adminOrder)) {
            log.error("映射到管理员失败，请管理员手动映射数据库！");
            log.info("订单ID：" + order.getId().toString());
        }

        return SaResult.ok("购买成功！");
    }

    /**
     * <p>取消订单服务</p>
     *
     * @param request 取消请求
     * @return 取消状态
     */
    @Override
    public SaResult orderCancel(OrderCancelRequest request) {
        Orders order = ordersService.getById(request.getOrderId());
        order.setState(State.valueOf("CANCEL"));
        Long productId = productOrderService.getProductId(order.getId());
        Products product = productsService.getProducts(productId);
        product.setNumber(product.getNumber() + order.getNumber());

        if (!ordersService.updateById(order) || !productsService.updateById(product)) {
            return SaResult.error("取消失败！");
        }
        return SaResult.ok("取消成功!");
    }

    /**
     * <p>获取登陆用户的订单信息</p>
     *
     * @return 订单信息
     */
    @Override
    public SaResult getMyOrder() {
        Long userId = Long.parseLong((String) StpUtil.getLoginId());
        List<Long> orderId = userOrderService.getOrderId(userId);
        if (orderId.isEmpty()) {
            return SaResult.error("无订单信息！");
        }
        List<Orders> orders = ordersService.getOrders(orderId);

        return SaResult.ok("获取订单信息成功!").setData(orders);
    }
}
