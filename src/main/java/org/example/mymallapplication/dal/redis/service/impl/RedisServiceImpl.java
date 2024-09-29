package org.example.mymallapplication.dal.redis.service.impl;

import cn.hutool.json.JSONUtil;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.person.Users;
import org.example.mymallapplication.dal.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * <p>是否存在key</p>
     *
     * @param key 键
     * @return 是否存在
     */
    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * <p>hash中是否存在某key</p>
     *
     * @param key     hash名
     * @param hashKey hash中的键
     * @return 是否存在
     */
    @Override
    public boolean hasKey(String key, String hashKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key)) && redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * <p>添加键值</p>
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void addKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * <p>添加hash键值</p>
     *
     * @param key     hash名
     * @param hashKey 键
     * @param value   值
     */
    @Override
    public void addHashKey(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * <p>存储用户实体类</p>
     *
     * @param key  键
     * @param user 用户实体类
     */
    @Override
    public void saveUserToRedis(String key, FrontendUsers user) {
        String jsonStr = JSONUtil.toJsonStr(user);
        redisTemplate.opsForValue().set(key, jsonStr);
    }

    /**
     * <p>获取用户实体类</p>
     *
     * @param key 键
     * @return 用户实体类
     */
    @Override
    public FrontendUsers getUserFromRedis(String key) {
        String jsonStr = (String) redisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(jsonStr, FrontendUsers.class);
    }

    /**
     * <p>获取值</p>
     *
     * @param key 键
     * @return 值
     */
    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * <p>获取hash值</p>
     *
     * @param key     hash名
     * @param hashKey hash键
     * @return 值
     */
    @Override
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * <p>设置过期时间</p>
     *
     * @param key    键
     * @param number 时间数字
     * @param unit   时间单位
     */
    @Override
    public void setExpire(String key, Long number, TimeUnit unit) {
        redisTemplate.expire(key, number, unit);
    }

    /**
     * <p>删除键</p>
     *
     * @param key 键
     * @return 是否删除成功
     */
    @Override
    public boolean deleteKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * <p>将管理员加入redis</p>
     *
     * @param key   键
     * @param users 管理员实体类
     */
    @Override
    public void saveAdminToRedis(String key, Users users) {
        String jsonStr = JSONUtil.toJsonStr(users);
        redisTemplate.opsForValue().set(key, jsonStr);
    }

    /**
     * <p>通过用户名查找管理员实体类</p>
     *
     * @param key 用户名
     * @return 管理员实体类
     */
    @Override
    public Users getAdmin(String key) {
        String jsonStr = (String) redisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(jsonStr, Users.class);
    }

    /**
     * <p>插入到延时队列中</p>
     *
     * @param key       键
     * @param id        ID
     * @param delayTime 延迟时间
     */
    @Override
    public void delayQueue(String key, String id, long delayTime) {
        redisTemplate.opsForZSet().add(key, id, delayTime);
    }
}
