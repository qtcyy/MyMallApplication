package org.example.mymallapplication.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import org.example.mymallapplication.dal.service.GroupService;
import org.example.mymallapplication.dal.vo.request.CategoryRequest;
import org.example.mymallapplication.dal.vo.request.GroupCateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chengyiyang
 */
@RestController
@RequestMapping("/category")
public class GroupController {
    @Autowired
    private GroupService categoryService;

    @SaCheckPermission("add")
    @PostMapping("/add")
    public SaResult saveCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.saveCate(request);
    }

    @SaCheckPermission("delete")
    @DeleteMapping("/delete/{id}")
    public SaResult deleteCategory(@PathVariable String id) {
        return categoryService.deleteCate(id);
    }

    @SaCheckPermission("read")
    @GetMapping("/info/{name}")
    public SaResult getCategory(@PathVariable String name) {
        return categoryService.getCate(name);
    }

    @SaCheckPermission("read")
    @GetMapping("/get/{id}")
    public SaResult getCategoryById(@PathVariable String id) {
        return categoryService.getCateById(id);
    }

    @SaCheckPermission("change")
    @PostMapping("/change/{id}")
    public SaResult changeCate(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.changeCate(id, request);
    }

    @SaCheckPermission(value = {"add", "change"}, mode = SaMode.OR)
    @PostMapping("/group")
    public SaResult groupCate(@Valid @RequestBody GroupCateRequest request) {
        return categoryService.groupCate(request);
    }

    @SaCheckPermission("delete")
    @DeleteMapping("/group/delete")
    public SaResult deleteGroup(@Valid @RequestBody GroupCateRequest request) {
        return categoryService.deleteGroup(request);
    }
}
