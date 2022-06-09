package org.isheihei.redis.core.obj.impl;

import org.isheihei.redis.core.obj.AbstractRedisObject;
import org.isheihei.redis.core.struct.RedisDataStruct;
import org.isheihei.redis.core.struct.RedisDataStructType;

/**
 * @ClassName: RedisListObject
 * @Description: Redis列表对象
 * @Date: 2022/5/31 13:05
 * @Author: isheihei
 */
public class RedisListObject extends AbstractRedisObject {
    private RedisDataStruct list;
    public RedisListObject() {
        setEncoding(RedisDataStructType.redisDoubleLinkedList);
        list = getEncoding().getSupplier().get();
    }

    public RedisListObject(RedisDataStructType encoding) {
        super(encoding);
        list = getEncoding().getSupplier().get();
    }

    @Override
    public RedisDataStruct data() {
        return list;
    }
}
