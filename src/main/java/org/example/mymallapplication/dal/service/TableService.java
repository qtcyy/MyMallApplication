package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;

import java.util.List;

/**
 * @author chengyiyang
 */
public interface TableService {
    SaResult getProduct(String name);

    SaResult buyProduct(BuyProductRequest request);

    SaResult buyProductByIds(List<BuyProductRequest> requests);

    SaResult orderCancel(OrderCancelRequest request);

    SaResult getMyOrder();

    void toFifteenDaysQueue(List<String> orderIds);
}
