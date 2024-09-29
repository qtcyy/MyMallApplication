package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.Balance;
import org.example.mymallapplication.dal.dao.mapper.person.BalanceMapper;
import org.example.mymallapplication.dal.dao.service.person.IBalanceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-25
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {

    /**
     * <p>根据用户ID获取余额实体类</p>
     *
     * @param userId 用户ID
     * @return 余额实体类
     */
    @Override
    public Balance getBalanceByUserId(String userId) {
        LambdaQueryWrapper<Balance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Balance::getUserId, userId).last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    /**
     * <p>根据管理员ID列表获取余额</p>
     *
     * @param adminIds 管理员ID列表
     * @return 管理员ID
     */
    @Override
    public List<Balance> getBalance(List<String> adminIds) {
        LambdaQueryWrapper<Balance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Balance::getUserId, adminIds);

        return this.list(queryWrapper);
    }
}
