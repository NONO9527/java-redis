package org.isheihei.redis.core.command.impl.zset;

import io.netty.channel.ChannelHandlerContext;
import org.isheihei.redis.common.consts.ErrorsConst;
import org.isheihei.redis.core.client.RedisClient;
import org.isheihei.redis.core.command.AbstractCommand;
import org.isheihei.redis.core.command.CommandType;
import org.isheihei.redis.core.obj.RedisObject;
import org.isheihei.redis.core.obj.impl.RedisZSetObject;
import org.isheihei.redis.core.resp.impl.Errors;
import org.isheihei.redis.core.resp.impl.RespInt;
import org.isheihei.redis.core.struct.RedisDataStruct;
import org.isheihei.redis.core.struct.impl.BytesWrapper;
import org.isheihei.redis.core.struct.impl.RedisZSet;

/**
 * @ClassName: ZCard
 * @Description: 返回有序集的成员个数
 * @Date: 2022/6/11 15:52
 * @Author: isheihei
 */
public class ZCard extends AbstractCommand {

    private BytesWrapper key;

    @Override
    public CommandType type() {
        return CommandType.zcard;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RedisClient redisClient) {
        if ((key = getBytesWrapper(ctx, array, 1)) == null) {
            return;
        }
        RedisObject redisObject = redisClient.getDb().get(key);
        if (redisObject == null) {
            ctx.writeAndFlush(new RespInt(0));
            return;
        }
        if (redisObject instanceof RedisZSetObject) {
            RedisDataStruct data = redisObject.data();
            if (data instanceof RedisZSet) {
                RedisZSet zSet = (RedisZSet) data;
                ctx.writeAndFlush(new RespInt(zSet.size()));
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            ctx.writeAndFlush(new Errors(ErrorsConst.WRONG_TYPE_OPERATION));
        }
    }
}
