package org.example.mymallapplication.dal.dao.service.person.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.person.Cart;
import org.example.mymallapplication.dal.dao.mapper.person.CartMapper;
import org.example.mymallapplication.dal.dao.service.person.ICartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-30
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    /**
     * 检查并返回特定购物车
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 特定购物车
     */
    @Override
    public Cart getCart(String userId, String productId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId).eq(Cart::getProductId, productId).last("limit 1");

        return getOne(queryWrapper);
    }

    /**
     * 获取购物车
     *
     * @param userId 用户ID
     * @return 购物车信息
     */
    @Override
    public List<Cart> getCart(String userId) {
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);

        return this.list(cartWrapper);
    }

    /**
     * 获取商品ID列表
     *
     * @param userId 用户ID
     * @return 商品ID列表
     */
    @Override
    public List<String> getProductIds(String userId) {
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);

        return this.list(cartWrapper).stream()
                .map(Cart::getProductId).toList();
    }
}
