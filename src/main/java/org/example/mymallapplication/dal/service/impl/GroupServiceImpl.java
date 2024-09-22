package org.example.mymallapplication.dal.service.impl;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Category;
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
    public SaResult deleteCate(Long id) {
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
        if (categories == null) {
            return SaResult.error("未找到");
        }

        List<Long> cateIds = categories.stream().map(Category::getId).toList();

        List<Long> productIds = productCateService.getProductIds(cateIds);
        List<Products> products = productsService.getProducts(productIds);

        return SaResult.ok("获取成功！").setData(new GetCateResponse(categories, products));
    }

    /**
     * <p>根据ID更改类别信息</p>
     *
     * @param id      类别ID
     * @param request 请求信息
     * @return 更改状态
     */
    @Override
    public SaResult changeCate(Long id, CategoryRequest request) {
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

        List<Long> errList = new ArrayList<>();
        for (Long id : request.getProductIds()) {
            if (!productsService.hasProduct(id)) {
                errList.add(id);
                continue;
            }
            productCateService.saveGroup(request.getCateId(), id);
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

        List<Long> errList = new ArrayList<>();
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

        if (errList.isEmpty()) {
            return SaResult.ok("删除成功！");
        }
        return SaResult.error("有错误！已返回有问题的产品ID").setData(errList);
    }
}
