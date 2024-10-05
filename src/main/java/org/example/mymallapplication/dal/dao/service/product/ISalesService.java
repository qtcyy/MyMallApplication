package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Products;
import org.example.mymallapplication.dal.dao.entity.product.Sales;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-03
 */
public interface ISalesService extends IService<Sales> {

    IPage<Sales> getSalesPage(Page<Sales> salesPage, List<Products> products);

    IPage<Sales> geySalesPageDsc(Page<Sales> salesPage, List<Products> products);
}
