package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Category;
import org.example.mymallapplication.dal.dao.entity.product.ProductCate;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.service.product.ICategoryService;
import org.example.mymallapplication.dal.dao.service.product.IProductCateService;
import org.example.mymallapplication.dal.dao.service.product.IProductsService;
import org.example.mymallapplication.dal.service.GroupService;
import org.example.mymallapplication.dal.vo.request.CategoryRequest;
import org.example.mymallapplication.dal.vo.request.GroupCateRequest;
import org.example.mymallapplication.dal.vo.response.GetCateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductCateService productCateService;
    @Autowired
    private IProductsService productsService;

    /**
     * <p>保存商品类别</p>
     *
     * @param request 商品类别
     * @return 保存状态
     */
    @Override
    public SaResult saveCate(CategoryRequest request) {
        if (request.getName() == null || request.getDescription() == null) {
            return SaResult.error("有空变量！");
        }
        if (categoryService.hasCate(request.getName())) {
            return SaResult.error("已存在类别!");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (categoryService.saveCate(category)) {
            return SaResult.ok("保存成功！");
        }

        return SaResult.error("保存失败！");
    }

    /**
     * <p>根据ID删除商品种类</p>
     *
     * @param id 商品种类ID
     * @return 删除状态
     */
    @Override
    public SaResult deleteCate(String id) {
        if (!categoryService.hasCate(id)) {
            return SaResult.error("不存在类别");
        }

        //包含删除映射信息
        if (productCateService.deleteByCate(id) && categoryService.deleteCate(id)) {
            return SaResult.ok("删除成功");
        }
        return SaResult.error("删除失败！");
    }

    /**
     * <p>近似查找匹配的类别</p>
     *
     * @param name 查找名
     * @return 查找结果(包含类别下所有产品的信息)
     */
    @Override
    public SaResult getCate(String name) {
        List<Category> categories = categoryService.getCategories(name);
        if (categories.isEmpty()) {
            return SaResult.error("未找到");
        }

        List<String> cateIds = categories.stream().map(Category::getId).toList();

        List<String> productIds = productCateService.getProductIds(cateIds);
        if (productIds.isEmpty()) {
            return SaResult.ok("获取成功！无相关产品").setData(categories).setCode(520);
        }
        System.out.println(productIds);
        List<Products> products = productsService.getProducts(productIds);

        return SaResult.ok("获取成功！").setData(new GetCateResponse(categories, products));
    }

    /**
     * <p>根据ID获取类别</p>
     *
     * @param id 类别ID
     * @return 类别
     */
    @Override
    public SaResult getCateById(String id) {
        if (!categoryService.hasCate(id)) {
            return SaResult.error("不存在！");
        }

        Category category = categoryService.getById(id);
        return SaResult.ok("获取成功!").setData(category);
    }

    /**
     * <p>根据ID更改类别信息</p>
     *
     * @param id      类别ID
     * @param request 请求信息
     * @return 更改状态
     */
    @Override
    public SaResult changeCate(String id, CategoryRequest request) {
        if (!categoryService.hasCate(id)) {
            return SaResult.error("无此类别！");
        }

        Category category = categoryService.getById(id);
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        if (categoryService.updateCategory(id, category)) {
            return SaResult.ok("修改成功！");
        }

        return SaResult.error("修改失败!");
    }

    /**
     * <p>给商品归类到对应的类别</p>
     *
     * @param request 归类请求
     * @return 归类状态
     */
    @Override
    public SaResult groupCate(GroupCateRequest request) {
        if (!categoryService.hasCate(request.getCateId())) {
            return SaResult.error("无此类别！");
        }

        List<String> productIds = request.getProductIds();
        List<String> existingIds = productsService.getExistingProductIds(productIds);

        List<String> errList = new ArrayList<>(productIds);
        errList.removeAll(existingIds);

        List<ProductCate> entities = existingIds.stream()
                .map(id -> {
                    ProductCate entity = new ProductCate();
                    entity.setCategoryId(request.getCateId());
                    entity.setProductId(id);
                    return entity;
                }).toList();

        if (!entities.isEmpty()) {
            productCateService.saveBatch(entities);
        }

        if (errList.isEmpty()) {
            return SaResult.ok("保存成功！");
        }
        return SaResult.error("有产品不存在，已返回ID").setData(errList);
    }

    /**
     * <p>删除分组分类</p>
     *
     * @param request 删除请求
     * @return 删除状态
     */
    @Override
    public SaResult deleteGroup(GroupCateRequest request) {
        if (!categoryService.hasCate(request.getCateId())) {
            return SaResult.error("无此类别！");
        }

        /*
        for (Long id : request.getProductIds()) {
            if (!productsService.hasProduct(id)) {
                errList.add(id);
                continue;
            }
            if (!productCateService.hasGroup(request.getCateId(), id)) {
                errList.add(id);
                continue;
            }

            productCateService.deleteGroup(request.getCateId(), id);
        }
        */
        List<String> productIds = request.getProductIds();
        List<String> existingIds = productsService.getExistingProductIds(productIds);

        List<String> errList = new ArrayList<>(productIds);
        errList.removeAll(existingIds);
        List<ProductCate> delProductCate = productCateService.getProducts(existingIds);

        if (productCateService.removeBatchByIds(delProductCate)) {
            if (errList.isEmpty()) {
                return SaResult.ok("删除成功！");
            }
            return SaResult.error("有错误！已返回有问题的产品ID").setData(errList);
        }

        return SaResult.error("删除失败！");
    }
}
