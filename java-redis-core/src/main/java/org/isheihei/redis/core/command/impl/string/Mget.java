package org.isheihei.redis.core.command.impl.string;

import com.sun.javafx.image.impl.ByteRgb;
import io.netty.channel.ChannelHandlerContext;
import org.isheihei.redis.core.client.RedisClient;
import org.isheihei.redis.core.command.Command;
import org.isheihei.redis.core.command.CommandType;
import org.isheihei.redis.core.db.RedisDB;
import org.isheihei.redis.core.obj.impl.RedisStringObject;
import org.isheihei.redis.core.resp.BulkString;
import org.isheihei.redis.core.resp.Resp;
import org.isheihei.redis.core.resp.RespArray;
import org.isheihei.redis.core.struct.RedisDataStruct;
import org.isheihei.redis.core.struct.impl.BytesWrapper;
import org.isheihei.redis.core.struct.impl.RedisDynamicString;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: Mget
 * @Description: 批量获取，不存在的键返回空
 * @Date: 2022/6/9 23:24
 * @Author: isheihei
 */
public class Mget implements Command {

    private List<BytesWrapper> keys;

    @Override
    public CommandType type() {
        return CommandType.mget;
    }

    @Override
    public void setContent(Resp[] array) {
        keys = Arrays.stream(array).skip(1).map(resp -> ((BulkString) resp).getContent()).collect(Collectors.toList());
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RedisClient redisClient) {
        if (keys.size() == 0 || keys == null) {
            ctx.writeAndFlush(BulkString.NullBulkString);
            return;
        }
        RedisDB db = redisClient.getDb();
        Resp[] array = keys.stream()
                .map(key -> db.get(key))
                .map(redisObject -> {
                    if (redisObject == null) {
                        return BulkString.NullBulkString;
                    } else {
                        if (redisObject instanceof RedisStringObject) {
                            RedisDataStruct data = redisObject.data();
                            if (data instanceof RedisDynamicString) {
                                BytesWrapper value = ((RedisDynamicString) data).getValue();
                                return new BulkString(value);
                            } else {
                                throw new UnsupportedOperationException();
                            }
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                })
                .toArray(Resp[]::new);
        ctx.writeAndFlush(new RespArray(array));
    }
}
