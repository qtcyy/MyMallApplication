package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.AdminOrder;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-23
 */
public interface IAdminOrderService extends IService<AdminOrder> {

    String getAdminId(String orderId);

    IPage<AdminOrder> getEntitiesPage(IPage<AdminOrder> page, List<Long> orderIds);

    List<AdminOrder> getAdminIds(List<String> orderIds);
}
