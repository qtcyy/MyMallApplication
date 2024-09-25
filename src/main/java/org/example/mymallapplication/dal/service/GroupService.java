package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.vo.request.CategoryRequest;
import org.example.mymallapplication.dal.vo.request.GroupCateRequest;

public interface GroupService {

    SaResult saveCate(CategoryRequest request);

    SaResult deleteCate(Long id);

    SaResult getCate(String name);

    SaResult getCate(Long id);

    SaResult changeCate(Long id, CategoryRequest request);

    SaResult groupCate(GroupCateRequest request);

    SaResult deleteGroup(GroupCateRequest request);
}
