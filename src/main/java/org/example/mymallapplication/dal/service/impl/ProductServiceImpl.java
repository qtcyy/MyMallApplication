package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.example.mymallapplication.common.BaseContext;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.service.product.ICategoryService;
import org.example.mymallapplication.dal.dao.service.product.IOrdersService;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.example.mymallapplication.dal.enums.State;
import org.example.mymallapplication.dal.service.ProductService;
import org.example.mymallapplication.dal.service.TableService;
import org.example.mymallapplication.dal.vo.request.ShippingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private IProductsService productsService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private TableService tableService;

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
     * <p>更新产品信息</p>
     *
     * @param newProduct 接收到的产品信息
     * @return 状态
     */
    @Override
    public SaResult changeProduct(Products newProduct) {
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
    public SaResult shipProduct(ShippingRequest request) {
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

}
