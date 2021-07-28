package cn.edu.guet.popular_blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author pangjian
 * @ClassName RedisUtil
 * @Description redis工具类
 * @date 2021/7/22 12:10
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @Description: 设置键值的同时设置一天有效时间
     * @Param key:
     * @Param value:
     * @return void
     * @date 2021/7/22 12:22
    */
    public void creatByTimeout(String key, String value){
        redisTemplate.opsForValue().set(key, value, 24*60*60);
    }

    /**
     * @Description: 根据key取到value
     * @Param key:
     * @return java.lang.String
     * @date 2021/7/22 16:03
    */
    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }


}
