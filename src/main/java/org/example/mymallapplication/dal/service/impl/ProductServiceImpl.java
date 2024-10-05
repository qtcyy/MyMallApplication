package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.mymallapplication.common.BaseContext;
import org.example.mymallapplication.dal.dao.entity.commit.ProductReviews;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.entity.product.Sales;
import org.example.mymallapplication.dal.dao.service.commit.IProductReviewsService;
import org.example.mymallapplication.dal.dao.service.product.*;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.service.ProductService;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.ShippingRequest;
import org.example.mymallapplication.dal.vo.response.GetProductResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chengyiyang
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private IProductsService productsService;
    @Autowired
    private IProductCateService productCateService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private TableService tableService;
    @Autowired
    private IProductReviewsService productReviewsService;
    @Autowired
    private ISalesService salesService;

    /**
     * <p>保存商品信息</p>
     *
     * @param product 商品
     * @return 保存状态
     */
    @Override
    public SaResult saveProduct(Products product) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        if (productsService.saveProduct(product)) {
            BaseContext.clear();
            return SaResult.ok("保存成功！");
        }

        BaseContext.clear();
        return SaResult.error("保存失败!");
    }

    /**
     * <p>根据产品名搜索产品信息</p>
     *
     * @param name 产品名
     * @return 含有关键字的产品信息
     */
    @Override
    public SaResult getProductsLike(String name) {
        List<Products> products = productsService.getProductsLike(name);
        if (products.isEmpty()) {
            return SaResult.error("未找到");
        }
        return SaResult.ok("获取成功！").setData(products);
    }

    /**
     * 分页查询商品
     *
     * @param page 页面
     * @param size 页面大小
     * @return 搜索信息
     */
    @Override
    public SaResult getProductsPage(String name, int page, int size) {
        Page<Products> productsPage = new Page<>(page, size);
        IPage<Products> mainPages = productsService.getProductPage(productsPage, name);

        return SaResult.ok()
                .setData(mainPages.getRecords())
                .set("total", mainPages.getTotal())
                .set("page", page)
                .set("size", size);
    }

    /**
     * <p>根据分组ID获取产品信息</p>
     *
     * @param groupId 分组ID
     * @return 产品信息
     */
    @Override
    public SaResult getProductsGroup(String groupId) {
        List<String> productIds = productCateService.getProductIds(List.of(groupId));
        try {
            List<Products> products = productsService.getProducts(productIds);
            if (products.isEmpty()) {
                return SaResult.error("无商品");
            }
            return SaResult.ok("查询成功").setData(products);
        } catch (Exception e) {
            return SaResult.error("查询失败");
        }
    }

    /**
     * <p>更新产品信息</p>
     *
     * @param newProduct 接收到的产品信息
     * @return 状态
     */
    @Override
    public SaResult changeProduct(@NotNull Products newProduct) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        Products product = productsService.getProducts(newProduct.getId());

        BeanUtil.copyProperties(newProduct, product, CopyOptions.create().setIgnoreNullValue(true));

        if (productsService.changeProduct(product)) {
            BaseContext.clear();
            return SaResult.ok("更新成功!");
        }

        BaseContext.clear();
        return SaResult.error("更新失败!");
    }

    /**
     * <p>通过ID删除商品</p>
     *
     * @param id 商品ID
     * @return 删除状态
     */
    @Override
    public SaResult deleteProduct(String id) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        if (productsService.deleteProduct(id)) {
            BaseContext.clear();
            return SaResult.ok("删除成功！");
        }
        BaseContext.clear();
        return SaResult.error("删除失败！");
    }

    /**
     * <p>订单发货服务</p>
     *
     * @param request 发货请求
     * @return 发货状态
     */
    @Override
    public SaResult shipProduct(@NotNull ShippingRequest request) {
        BaseContext.setCurrentId(StpUtil.getLoginIdAsString());
        List<String> orderIds = request.getOrderIds();
        List<Orders> orders = ordersService.getOrders(orderIds);
        orders.forEach(order -> {
            order.setState(State.valueOf("TRANSPORT"));
            order.setShippingTime(LocalDateTime.now());
        });
        ordersService.saveBatch(orders);
        tableService.toFifteenDaysQueue(orderIds);

        BaseContext.clear();
        return SaResult.ok("发货成功！");
    }

    /**
     * <p>获取商品平均评分</p>
     *
     * @param id 商品ID
     * @return 评分
     */
    @Override
    public SaResult getStar(String id) {
        List<ProductReviews> productReviews = productReviewsService.getReviewsByProductId(id);
        if (productReviews.isEmpty()) {
            return SaResult.error("无评论");
        }

        Double star = 0.0;
        for (ProductReviews review : productReviews) {
            star += review.getRating();
        }
        star = star / productReviews.size();

        return SaResult.ok("获取成功！").set("star", star);
    }

    /**
     * <p>获取商品差评</p>
     *
     * @param id   商品ID
     * @param page 页码
     * @param size 页面大小
     * @return 差评
     */
    @Override
    public SaResult getBadComment(String id, int page, int size) {
        Page<ProductReviews> reviewsPage = new Page<>(page, size);
        IPage<ProductReviews> reviews = productReviewsService.getBadReviews(id, reviewsPage);

        return SaResult.ok("获取成功！")
                .set("data", reviews.getRecords())
                .set("total", reviews.getTotal())
                .set("page", page)
                .set("size", size);
    }

    /**
     * <p>获取商品好评</p>
     *
     * @param id   商品ID
     * @param page 页码
     * @param size 页面大小
     * @return 好评
     */
    @Override
    public SaResult getGoodComment(String id, int page, int size) {
        Page<ProductReviews> reviewsPage = new Page<>(page, size);
        IPage<ProductReviews> reviews = productReviewsService.getGoodReviews(id, reviewsPage);

        return SaResult.ok("获取成功！")
                .set("data", reviews.getRecords())
                .set("total", reviews.getTotal())
                .set("page", page)
                .set("size", size);
    }

    /**
     * <p>按销量降序商品信息</p>
     *
     * @param name 商品名
     * @param page 当前页面
     * @param size 页面大小
     * @return 商品信息
     */
    @Override
    public SaResult getProductsPageDesc(String name, int page, int size) {
        Page<Sales> salesPage = new Page<>(page, size);

        List<Products> products = productsService.getProductsLike(name);
        Map<String, Products> productsMap = products.stream().collect(Collectors.toMap(Products::getId, p -> p));
        IPage<Sales> salesIPage = salesService.getSalesPage(salesPage, products);
        List<GetProductResponse> sales = salesIPage.getRecords().stream()
                .map(sale -> {
                    GetProductResponse response = new GetProductResponse();
                    Products product = productsMap.get(sale.getProductId());
                    BeanUtil.copyProperties(product, response);
                    response.setCount(sale.getCount());

                    return response;
                }).toList();

        return SaResult.ok("获取成功！")
                .set("data", sales)
                .set("total", salesIPage.getTotal())
                .set("page", page)
                .set("size", size);
    }

    /**
     * 按销量升序商品信息
     *
     * @param name 商品名
     * @param page 当前页面
     * @param size 页面大小
     * @return 商品信息
     */
    @Override
    public SaResult getProductsPageDsc(String name, int page, int size) {
        Page<Sales> salesPage = new Page<>(page, size);

        List<Products> products = productsService.getProductsLike(name);
        Map<String, Products> productsMap = products.stream().collect(Collectors.toMap(Products::getId, p -> p));
        IPage<Sales> salesIPage = salesService.geySalesPageDsc(salesPage, products);
        List<GetProductResponse> sales = salesIPage.getRecords().stream()
                .map(sale -> {
                    GetProductResponse response = new GetProductResponse();
                    Products product = productsMap.get(sale.getProductId());
                    BeanUtil.copyProperties(product, response);
                    response.setCount(sale.getCount());

                    return response;
                }).toList();

        return SaResult.ok("获取成功！")
                .set("data", sales)
                .set("total", salesIPage.getTotal())
                .set("page", page)
                .set("size", size);
    }

    @Override
    public SaResult getProductsPageRange(String name, int minPrice, int maxPrice, int page, int size) {
        Page<Products> productsPage = new Page<>(page, size);
        try {
            IPage<Products> mainPages = productsService.getProductRange(productsPage, name, minPrice, maxPrice);

            return SaResult.ok()
                    .setData(mainPages.getRecords())
                    .set("total", mainPages.getTotal())
                    .set("page", page)
                    .set("size", size);
        } catch (Exception e) {
            return SaResult.error("查询失败");
        }
    }
}
