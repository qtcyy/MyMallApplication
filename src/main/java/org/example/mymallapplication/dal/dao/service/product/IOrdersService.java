package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.example.mymallapplication.dal.enums.State;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-20
 */
public interface IOrdersService extends IService<Orders> {

    List<Orders> getOrders(List<String> orderIds);

    IPage<Orders> getOrders(IPage<Orders> page, List<String> ids);

    IPage<Orders> getOrderPageByState(IPage<Orders> page, State state);

    List<Orders> getOrderByState(State state);

    List<Orders> getOrdersWithState(List<String> orderIds, State state);

    List<Orders> getOrderToConfirm(State state, LocalDateTime fifteenDaysTime);
}
