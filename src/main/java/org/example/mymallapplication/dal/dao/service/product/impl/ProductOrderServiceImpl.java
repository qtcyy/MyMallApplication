package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.ProductOrder;
import org.example.mymallapplication.dal.dao.mapper.product.ProductOrderMapper;
import org.example.mymallapplication.dal.dao.service.product.IProductOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements IProductOrderService {

}
