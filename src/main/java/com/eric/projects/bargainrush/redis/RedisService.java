package com.eric.projects.bargainrush.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

@Configuration
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String actualKey = formatKey(keyPrefix, key);
            String strValue = jedis.get(actualKey);
            T value = stringToBean(strValue, clazz);
            return value;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix keyPrefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String strValue = beanToString(value);
            if (!StringUtils.hasText(strValue)) {
                return false;
            }
            String realKey = formatKey(keyPrefix, key);
            if (keyPrefix.expireSeconds() <= 0) {
                jedis.set(realKey, strValue);
            } else {
                jedis.setex(realKey, keyPrefix.expireSeconds(), strValue);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = formatKey(keyPrefix, key);
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = formatKey(keyPrefix, key);
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = formatKey(keyPrefix, key);
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    private <T> T stringToBean(String str, Class<T> clazz) {
        if(!StringUtils.hasText(str) || clazz == null) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private <T> String beanToString(T value) {
        if (Objects.isNull(value)) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return String.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }


    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    private String formatKey(KeyPrefix keyPrefix, String key) {
        return keyPrefix.getPrefix() + ":" + key;
    }

}
