package org.example.mymallapplication.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @SaCheckPermission("add")
    @RequestMapping("/insert")
    public SaResult saveProduct(@RequestBody Products product) {
        return productService.saveProduct(product);
    }

    @SaIgnore
    @GetMapping("/info/{name}")
    public SaResult getProducts(@PathVariable String name) {
        return productService.getProductsLike(name);
    }

    @SaCheckPermission("change")
    @RequestMapping("/change")
    public SaResult changeProduct(@RequestBody Products product) {
        return productService.changeProduct(product);
    }

    @SaCheckPermission("delete")
    @DeleteMapping("/delete/{id}")
    public SaResult deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
