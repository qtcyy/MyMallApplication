package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.AdminOrder;
import org.example.mymallapplication.dal.dao.mapper.product.AdminOrderMapper;
import org.example.mymallapplication.dal.dao.service.product.IAdminOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-23
 */
@Service
public class AdminOrderServiceImpl extends ServiceImpl<AdminOrderMapper, AdminOrder> implements IAdminOrderService {

}
