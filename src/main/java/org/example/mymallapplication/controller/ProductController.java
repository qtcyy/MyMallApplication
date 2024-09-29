package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.service.ProductService;
import org.example.mymallapplication.dal.vo.request.ShippingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @SaCheckRole(value = {"root", "admin"}, mode = SaMode.OR)
    @GetMapping("/isLogin")
    public SaResult loginCheck() {
        return SaResult.ok("已登陆!");
    }

    @SaCheckPermission("add")
    @PostMapping("/insert")
    public SaResult saveProduct(@RequestBody Products product) {
        return productService.saveProduct(product);
    }

    @SaIgnore
    @GetMapping("/info/{name}")
    public SaResult getProducts(@PathVariable String name) {
        return productService.getProductsLike(name);
    }

    @SaCheckPermission("change")
    @PostMapping("/change")
    public SaResult changeProduct(@RequestBody Products product) {
        return productService.changeProduct(product);
    }

    @SaCheckPermission("delete")
    @DeleteMapping("/delete/{id}")
    public SaResult deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @SaCheckPermission(value = {"change", "add"}, mode = SaMode.OR)
    @PostMapping("/shipping")
    public SaResult shipProduct(@RequestBody ShippingRequest request) {
        return productService.shipProduct(request);
    }
}
