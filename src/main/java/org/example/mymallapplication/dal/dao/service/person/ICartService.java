package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.Cart;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-30
 */
public interface ICartService extends IService<Cart> {

    Cart getCart(String userId, String productId);

    List<Cart> getCart(String userId);

    List<String> getProductIds(String userId);
}
