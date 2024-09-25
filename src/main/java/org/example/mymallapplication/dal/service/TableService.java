package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;

public interface TableService {
    SaResult getProduct(String name);

    SaResult buyProduct(BuyProductRequest request);

    SaResult orderCancel(OrderCancelRequest request);

    SaResult getMyOrder();
}
