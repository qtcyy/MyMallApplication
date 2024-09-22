package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.Permissions;
import org.example.mymallapplication.dal.dao.mapper.person.PermissionsMapper;
import org.example.mymallapplication.dal.dao.service.person.IPermissionsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {
    /**
     * <p>获取权限字符串列表</p>
     *
     * @param ids 权限ID列表
     * @return 权限字符串列表
     */
    @Override
    public List<String> getPermissions(List<Long> ids) {
        LambdaQueryWrapper<Permissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permissions::getId, ids).select(Permissions::getPermission);
        return this.baseMapper.selectObjs(queryWrapper).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
