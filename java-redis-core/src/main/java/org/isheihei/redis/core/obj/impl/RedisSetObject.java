package org.isheihei.redis.core.obj.impl;

import org.isheihei.redis.core.obj.AbstractRedisObject;
import org.isheihei.redis.core.struct.RedisDataStruct;
import org.isheihei.redis.core.struct.RedisDataStructType;

/**
 * @ClassName: RedisSetObject
 * @Description: Redis集合对象
 * @Date: 2022/5/31 13:06
 * @Author: isheihei
 */
public class RedisSetObject extends AbstractRedisObject {

    private RedisDataStruct set;

    public RedisSetObject() {
        setEncoding(RedisDataStructType.redisSet);
        set = getEncoding().getSupplier().get();
    }

    public RedisSetObject(RedisDataStructType encoding) {
        super(encoding);
        set = getEncoding().getSupplier().get();
    }

    @Override
    public RedisDataStruct data() {
        return set;
    }
}
