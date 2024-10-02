package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.*;

import java.util.List;

/**
 * @author chengyiyang
 */
public interface UserService {

    SaResult userLogin(UserLoginRequest request);

    SaResult userRegister(UserRegisterRequest request);

    SaResult changePwd(ChangePwdRequest request);

    SaResult rechargeBalance(double money);

    SaResult withdrawBalance(double money);

    SaResult getUserInfo();

    SaResult setAddress(String address);

    SaResult confirmOrder(ConfirmOrderRequest request);

    SaResult writeCommit(WriteCommitRequest request);

    SaResult replyCommit(CommitReplyRequest request);

    SaResult likeCommit(String id);

    SaResult getCommit(String productId, int page, int size);

    SaResult addToCart(AddCartRequest request);

    SaResult getCart();

    SaResult getUnpaidOrder();

    SaResult payOrder(String orderId);

    SaResult payOrders(List<String> orderIds);
}
