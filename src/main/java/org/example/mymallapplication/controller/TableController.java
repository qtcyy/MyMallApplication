package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class TableController {
    @Autowired
    TableService tableService;

    @SaCheckLogin
    @RequestMapping("/product/search/{name}")
    public SaResult getProduct(@PathVariable String name) {
        return tableService.getProduct(name);
    }

    @SaCheckLogin
    @RequestMapping("/product/buy")
    public SaResult buyProduct(@RequestBody BuyProductRequest request) {
        return tableService.buyProduct(request);
    }

    @SaCheckLogin
    @RequestMapping("/product/cancel")
    public SaResult orderCancel(@RequestBody OrderCancelRequest request) {
        return tableService.orderCancel(request);
    }

    @SaCheckLogin
    @GetMapping("/order/info")
    public SaResult getMyOrder() {
        return tableService.getMyOrder();
    }
}
