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

/**
 * @author chengyiyang
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * 登陆验证
     */
    @SaCheckRole(value = {"root", "admin"}, mode = SaMode.OR)
    @GetMapping("/isLogin")
    public SaResult loginCheck() {
        return SaResult.ok("已登陆!");
    }

    /**
     * 添加商品
     */
    @SaCheckPermission("add")
    @PostMapping("/insert")
    public SaResult saveProduct(@RequestBody Products product) {
        return productService.saveProduct(product);
    }

    /**
     * 获取商品信息
     *
     * @param name 商品名
     */
    @SaIgnore
    @GetMapping("/info/{name}")
    public SaResult getProducts(@PathVariable String name) {
        return productService.getProductsLike(name);
    }

    /**
     * 获取组内商品信息
     *
     * @param groupId 商品组id
     * @return 商品信息
     */
    @SaIgnore
    @GetMapping("/info/getByGroup/{groupId}")
    public SaResult getProductsGroup(@PathVariable String groupId) {
        return productService.getProductsGroup(groupId);
    }

    /**
     * 获取商品信息
     *
     * @param name 商品名
     * @param page 页数
     * @param size 每页大小
     */
    @SaIgnore
    @GetMapping("/get/onPage")
    public SaResult getOnPage(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        return productService.getProductsPage(name, page, size);
    }

    /**
     * 获取商品信息
     *
     * @param name 商品名
     * @param page 当前页面
     * @param size 页面长度
     * @return 商品信息
     */
    @SaIgnore
    @GetMapping("/get/onPage/desc")
    public SaResult getOnPageDesc(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        return productService.getProductsPageDesc(name, page, size);
    }

    /**
     * 按价格范围获取商品信息
     *
     * @param name     商品名
     * @param minPrice 最小价格
     * @param maxPrice 最大价格
     * @param page     页数
     * @param size     每页大小
     * @return 商品信息
     */
    @SaIgnore
    @GetMapping("/get/onPage/range")
    public SaResult getOnPageRange(
            @RequestParam String name,
            @RequestParam int minPrice,
            @RequestParam int maxPrice,
            @RequestParam int page,
            @RequestParam int size) {
        return productService.getProductsPageRange(name, minPrice, maxPrice, page, size);
    }

    @SaIgnore
    @GetMapping("/get/onPage/dsc")
    public SaResult getOnPageDsc(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        return productService.getProductsPageDsc(name, page, size);
    }

    /**
     * 修改商品信息
     *
     * @param product 商品信息
     */
    @SaCheckPermission("change")
    @PostMapping("/change")
    public SaResult changeProduct(@RequestBody Products product) {
        return productService.changeProduct(product);
    }

    /**
     * 删除商品
     *
     * @param id 商品id
     */
    @SaCheckPermission("delete")
    @DeleteMapping("/delete/{id}")
    public SaResult deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    /**
     * 发货
     *
     * @param request 发货请求
     * @return 发货结果
     */
    @SaCheckPermission(value = {"change", "add"}, mode = SaMode.OR)
    @PostMapping("/shipping")
    public SaResult shipProduct(@RequestBody ShippingRequest request) {
        return productService.shipProduct(request);
    }

    /**
     * 获取商品平均star
     *
     * @param id 商品id
     * @return 评论
     */
    @SaCheckPermission(value = "read")
    @GetMapping("/commit/getStar/{id}")
    public SaResult getStar(@PathVariable String id) {
        return productService.getStar(id);
    }

    /**
     * 获取商品差评
     *
     * @param id   商品id
     * @param page 页数
     * @param size 每页大小
     * @return 评论
     */
    @SaCheckPermission(value = "read")
    @GetMapping("/commit/getBadComment/{id}")
    public SaResult getBadComment(@PathVariable String id, @RequestParam int page, @RequestParam int size) {
        return productService.getBadComment(id, page, size);
    }

    /**
     * 获取商品好评
     *
     * @param id   商品id
     * @param page 页数
     * @param size 每页大小
     * @return 评论
     */
    @SaCheckPermission(value = "read")
    @GetMapping("/commit/getGoodComment/{id}")
    public SaResult getGoodComment(@PathVariable String id, @RequestParam int page, @RequestParam int size) {
        return productService.getGoodComment(id, page, size);
    }
}
