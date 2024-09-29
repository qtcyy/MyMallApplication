package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.CategoryRequest;
import org.example.mymallapplication.dal.vo.request.GroupCateRequest;

public interface GroupService {

    SaResult saveCate(CategoryRequest request);

    SaResult deleteCate(String id);

    SaResult getCate(String name);

    SaResult getCateById(String id);

    SaResult changeCate(String id, CategoryRequest request);

    SaResult groupCate(GroupCateRequest request);

    SaResult deleteGroup(GroupCateRequest request);
}
