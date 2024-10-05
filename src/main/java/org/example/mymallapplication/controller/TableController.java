package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.BuyProductRequest;
import org.example.mymallapplication.dal.vo.request.OrderCancelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chengyiyang
 */
@RestController
@RequestMapping("/client")
public class TableController {
    @Autowired
    TableService tableService;

    @SaCheckLogin
    @GetMapping("/product/search/{name}")
    public SaResult getProduct(@PathVariable String name) {
        return tableService.getProduct(name);
    }

    @SaCheckLogin
    @PostMapping("/product/buy")
    public SaResult buyProduct(@Valid @RequestBody BuyProductRequest request) {
        return tableService.buyProduct(request);
    }

    @SaCheckLogin
    @PostMapping("/product/buy/list")
    public SaResult buyProductList(@NotBlank(message = "购买列表不能为空") List<BuyProductRequest> productIds) {
        return tableService.buyProductByIds(productIds);
    }

    @SaCheckLogin
    @PostMapping("/product/cancel")
    public SaResult orderCancel(@RequestBody OrderCancelRequest request) {
        return tableService.orderCancel(request);
    }

    @SaCheckLogin
    @GetMapping("/order/info")
    public SaResult getMyOrder() {
        return tableService.getMyOrder();
    }
}
