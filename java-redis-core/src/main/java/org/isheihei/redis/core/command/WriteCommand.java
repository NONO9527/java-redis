package org.isheihei.redis.core.command;

import io.netty.channel.ChannelHandlerContext;
import org.isheihei.redis.core.client.RedisClient;
import org.isheihei.redis.core.persist.aof.Aof;
import org.isheihei.redis.core.resp.Resp;
import org.isheihei.redis.core.resp.RespArray;

/**
 * @ClassName: AbstractCommand
 * @Description: 模板方法模式 实现后置 aof 等功能
 * @Date: 2022/6/13 20:48
 * @Author: isheihei
 */
public abstract class WriteCommand implements Command {

    public RespArray respArray;

    public Resp[] array;

    private Aof aof = null;

    @Override
    public void setContent(RespArray arrays) {
        this.respArray = arrays;
        this.array = arrays.getArray();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RedisClient redisClient) {
        handleWrite(ctx, redisClient);
        putAof();
    }

    public void setAof(Aof aof) {
        this.aof = aof;
    }

    public Aof getAof() {
        return aof;
    }

    public void putAof() {
        if (aof != null) {
            aof.put(respArray);
        }
    }

    /**
     * @Description: handle 处理操作
     * @Param: ctx
     * @Param: redisClient
     * @Author: isheihei
     */
    public abstract void handleWrite(ChannelHandlerContext ctx, RedisClient redisClient);

    /**
     * @Description: aof 载入操作
     * @Param: redisClient
     * @Author: isheihei
     */
    public abstract void handleLoadAof(RedisClient redisClient);

    @Override
    public abstract CommandType type();
}