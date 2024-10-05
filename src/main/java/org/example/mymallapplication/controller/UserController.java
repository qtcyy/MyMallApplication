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
    @PostMapping("/change/pwd")
    public SaResult changePwd(ChangePwdRequest request) {
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
    @PostMapping("/product/commit/")
    public SaResult writeCommit(@Valid @RequestBody WriteCommitRequest request) {
        return userService.writeCommit(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit/reply")
    public SaResult replyCommit(@Valid @RequestBody CommitReplyRequest request) {
        return userService.replyCommit(request);
    }

    @SaCheckLogin
    @PostMapping("/product/commit/image/")
    public SaResult addCommitImages(@RequestBody AddImageRequest request) {
        return userService.addReviewImage(request);
    }

    @SaCheckLogin
    @GetMapping("/product/commit/like/{id}")
    public SaResult likeCommit(@PathVariable String id) {
        return userService.likeCommit(id);
    }

    @SaIgnore
    @GetMapping("/product/commit/show/{productId}")
    public SaResult commit(
            @PathVariable String productId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return userService.getCommit(productId, page, size);
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
    @GetMapping("/cart")
    public SaResult getCart() {
        return userService.getCart();
    }

    @SaCheckLogin
    @GetMapping("/order/unpaid")
    public SaResult getUnpaidOrder() {
        return userService.getUnpaidOrder();
    }

    @SaCheckLogin
    @GetMapping("/order/pay/{orderId}")
    public SaResult payOrder(@NotBlank(message = "订单ID不能为空") @PathVariable String orderId) {
        return userService.payOrder(orderId);
    }

    @SaCheckLogin
    @PostMapping("/order/pay")
    public SaResult payOrders(@NotBlank(message = "订单ID不能为空") @RequestBody List<String> orderIds) {
        return userService.payOrders(orderIds);
    }
}
