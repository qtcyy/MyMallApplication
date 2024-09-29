package org.example.mymallapplication.dal.redis.service;

import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;
import org.example.mymallapplication.dal.dao.entity.person.Users;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    boolean hasKey(String key);

    boolean hasKey(String key, String hashKey);

    void addKey(String key, Object value);

    void addHashKey(String key, String hashKey, Object value);

    void saveUserToRedis(String key, FrontendUsers user);

    FrontendUsers getUserFromRedis(String key);

    Object getValue(String key);

    Object getHashValue(String key, String hashKey);

    void setExpire(String key, Long number, TimeUnit unit);

    boolean deleteKey(String key);

    void saveAdminToRedis(String key, Users users);

    Users getAdmin(String key);

    void delayQueue(String key, String id, long delayTime);
}
