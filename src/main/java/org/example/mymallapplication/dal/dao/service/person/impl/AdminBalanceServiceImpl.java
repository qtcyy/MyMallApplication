package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.AdminBalance;
import org.example.mymallapplication.dal.dao.mapper.person.AdminBalanceMapper;
import org.example.mymallapplication.dal.dao.service.person.IAdminBalanceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-28
 */
@Service
public class AdminBalanceServiceImpl extends ServiceImpl<AdminBalanceMapper, AdminBalance> implements IAdminBalanceService {

    /**
     * 根据管理员ID获取余额类
     *
     * @param adminId 管理员ID
     * @return 余额类
     */
    @Override
    public AdminBalance getAdminBalance(String adminId) {
        LambdaQueryWrapper<AdminBalance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminBalance::getAdminId, adminId).last("LIMIT 1");

        return this.list(queryWrapper).get(0);
    }
}
