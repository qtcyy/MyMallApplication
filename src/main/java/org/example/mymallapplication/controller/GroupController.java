package org.example.mymallapplication.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.service.GroupService;
import org.example.mymallapplication.dal.vo.request.CategoryRequest;
import org.example.mymallapplication.dal.vo.request.GroupCateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class GroupController {
    @Autowired
    private GroupService categoryService;

    @SaCheckPermission("add")
    @RequestMapping("/add")
    public SaResult saveCategory(@RequestBody CategoryRequest request) {
        return categoryService.saveCate(request);
    }

    @SaCheckPermission("delete")
    @RequestMapping("/delete/{id}")
    public SaResult deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCate(id);
    }

    @SaCheckPermission("read")
    @RequestMapping("/info/{name}")
    public SaResult getCategory(@PathVariable String name) {
        return categoryService.getCate(name);
    }

    @SaCheckPermission("change")
    @RequestMapping("/change/{id}")
    public SaResult changeCate(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return categoryService.changeCate(id, request);
    }

    @SaCheckPermission(value = {"add", "change"}, mode = SaMode.OR)
    @RequestMapping("/group")
    public SaResult groupCate(@RequestBody GroupCateRequest request) {
        return categoryService.groupCate(request);
    }

    @SaCheckPermission("delete")
    @DeleteMapping("/group/delete")
    public SaResult deleteGroup(@RequestBody GroupCateRequest request) {
        return categoryService.deleteGroup(request);
    }
}
