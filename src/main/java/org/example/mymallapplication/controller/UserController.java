package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.mymallapplication.dal.service.UserService;
import org.example.mymallapplication.dal.vo.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author chengyiyang
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @SaIgnore
    @GetMapping("/advertisement/show")
    public SaResult showAdvertisement() {
        return userService.showAdvertisement();
    }

    @SaIgnore
    @PostMapping("/login")
    public SaResult doLogin(@RequestBody UserLoginRequest request) {
        return userService.userLogin(request);
    }

    @SaIgnore
    @PostMapping("/register")
    public SaResult doRegister(@RequestBody UserRegisterRequest request) {
        return userService.userRegister(request);
    }

    @SaCheckLogin
    @GetMapping("/isLogin")
    public SaResult isLogin() {
        if (StpUtil.getRoleList().equals(Collections.emptyList())) {
            return SaResult.ok("普通用户登陆");
        } else {
            return SaResult.error("管理员禁用");
        }
    }

    @SaCheckLogin
    @GetMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("登出成功");
    }

    @SaCheckLogin
    @PostMapping("/change/pwd")
    public SaResult changePwd(@RequestBody ChangePwdRequest request) {
        return userService.changePwd(request);
    }

    @SaCheckLogin
    @GetMapping("/info/get")
    public SaResult getLoginUserInfo() {
        return userService.getUserInfo();
    }

    @SaCheckLogin
    @GetMapping("/balance/recharge/{money}")
    public SaResult rechargeBalance(@PathVariable double money) {
        return userService.rechargeBalance(money);
    }

    @SaCheckLogin
    @GetMapping("/balance/withdraw/{money}")
    public SaResult withdrawBalance(@PathVariable double money) {
        return userService.withdrawBalance(money);
    }

    @SaCheckLogin
    @PostMapping("/info/address/{address}")
    public SaResult setAddress(@PathVariable String address) {
        return userService.setAddress(address);
    }

    @SaCheckLogin
    @PostMapping("/order/confirm")
    public SaResult confirmOrder(@RequestBody ConfirmOrderRequest request) {
        return userService.confirmOrder(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit")
    public SaResult writeCommit(@Valid @RequestBody WriteCommitRequest request) {
        return userService.writeCommit(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit/reply")
    public SaResult replyCommit(@Valid @RequestBody CommitReplyRequest request) {
        return userService.replyCommit(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit/image")
    public SaResult addCommitImages(@RequestBody AddImageRequest request) {
        return userService.addReviewImage(request);
    }

    @SaCheckLogin
    @GetMapping("/product/commit/like")
    public SaResult likeCommit(@RequestParam String commitId) {
        return userService.likeCommit(commitId);
    }

    @SaIgnore
    @GetMapping("/product/commit/show")
    public SaResult commit(
            @RequestParam String productId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return userService.getCommit(productId, page, size);
    }

    @SaCheckLogin
    @GetMapping("/product/commit/show/self")
    public SaResult getSelfCommit(@RequestParam int page, @RequestParam int size) {
        return userService.getSelfCommit(page, size);
    }

    @SaIgnore
    @GetMapping("/product/commit/get/images/{reviewId}")
    public SaResult getCommitImages(@PathVariable String reviewId) {
        return userService.getCommitImages(reviewId);
    }

    @SaCheckLogin
    @PostMapping("/cart/add")
    public SaResult addCart(@Valid @RequestBody AddCartRequest request) {
        return userService.addToCart(request);
    }

    @SaCheckLogin
    @GetMapping("/cart/delete")
    public SaResult deletedCart(@RequestParam String productId) {
        return userService.deleteCart(productId);
    }

    @SaCheckLogin
    @PostMapping("/cart/change")
    public SaResult changeCart(@RequestParam String productId, @RequestParam int quantity) {
        return userService.changeCart(productId, quantity);
    }

    @SaCheckLogin
    @GetMapping("/cart")
    public SaResult getCart() {
        return userService.getCart();
    }

    @SaCheckLogin
    @GetMapping("/order/get")
    public SaResult getOrder(@RequestParam int page, @RequestParam int size) {
        return userService.getOrder(page, size);
    }

    @SaCheckLogin
    @GetMapping("/order/unpaid")
    public SaResult getUnpaidOrder() {
        return userService.getUnpaidOrder();
    }

    @SaCheckLogin
    @GetMapping("/order/pay")
    public SaResult payOrder(@NotBlank(message = "订单ID不能为空") @RequestParam String orderId) {
        return userService.payOrder(orderId);
    }

    @SaCheckLogin
    @PostMapping("/order/pay/list")
    public SaResult payOrders(@NotBlank(message = "订单ID不能为空") @RequestBody List<String> orderIds) {
        return userService.payOrders(orderIds);
    }

    @SaCheckLogin
    @PostMapping("/order/refund")
    public SaResult refundOrder(@Valid @RequestBody RefundOrderRequest request) {
        return userService.refundOrder(request);
    }
}
