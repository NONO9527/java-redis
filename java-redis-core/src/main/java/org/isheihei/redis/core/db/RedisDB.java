package org.isheihei.redis.core.db;

import org.isheihei.redis.core.obj.RedisObject;
import org.isheihei.redis.core.struct.impl.BytesWrapper;

import java.util.Set;

/**
 * @ClassName: RedisDB
 * @Description: 数据库接口
 * @Date: 2022/6/8 15:21
 * @Author: isheihei
 */
public interface RedisDB {
    /**
     * @Description: 获取所有键
     * @Return: Set<BytesWrapper>
     * @Author: isheihei
     */
    Set<BytesWrapper> keys();

    /**
     * @Description: 判断某个键是否存在
     * @Param: key
     * @Return: boolean
     * @Author: isheihei
     */
    boolean exist(BytesWrapper key);

    /**
     * @Description: put一个键值对
     * @Param: key
     * @Param: redisObject
     * @Return: void
     * @Author: isheihei
     */
    void put(BytesWrapper key, RedisObject redisObject);

    /**
     * @Description: 获取一个键对应的值 过期或不存在返回 null
     * @Param: key
     * @Return: RedisObject
     * @Author: isheihei
     */
    RedisObject get(BytesWrapper key);

    /**
     * @Description: 为一个键设置过期时间
     * @Param: key
     * @Param: expireTime
     * @Return: int 成功返回1 失败返回0
     * @Author: isheihei
     */
    int expire(BytesWrapper key, long expireTime);


    /**
     * @Description: 将一个 key 的过期时间删除
     * @Param: key
     * @Return: int
     * @Author: isheihei
     */
    int persist(BytesWrapper key);

    /**
     * @Description: 一个键是否过期 不存在或过期返回 false 未过期返回 true
     * @Param: key
     * @Return: boolean
     * @Author: isheihei
     */
    boolean isExpired(BytesWrapper key);

    /**
     * @Description: 获取过期列表的大小
     * @Return:
     * @Author: isheihei
     */
    int expiresSize();

    /**
     * @Description: 随机获取一个过期列表中的 key
     * @Return: BytesWrapper
     * @Author: isheihei
     */
    BytesWrapper getRandomKey();

    /**
     * @Description: 删除过期的键
     * @Author: isheihei
     */
    void deleteExpiredKey(BytesWrapper key);

    /**
     * @Description: 清空数据库
     * @Return: void
     * @Author: isheihei
     */
    void cleanAll();
}
