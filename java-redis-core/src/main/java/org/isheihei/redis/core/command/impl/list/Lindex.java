package org.isheihei.redis.core.command.impl.list;

import io.netty.channel.ChannelHandlerContext;
import org.isheihei.redis.common.consts.ErrorsConsts;
import org.isheihei.redis.core.client.RedisClient;
import org.isheihei.redis.core.command.Command;
import org.isheihei.redis.core.command.CommandType;
import org.isheihei.redis.core.obj.RedisObject;
import org.isheihei.redis.core.obj.impl.RedisListObject;
import org.isheihei.redis.core.resp.BulkString;
import org.isheihei.redis.core.resp.Errors;
import org.isheihei.redis.core.resp.Resp;
import org.isheihei.redis.core.struct.RedisDataStruct;
import org.isheihei.redis.core.struct.impl.BytesWrapper;
import org.isheihei.redis.core.struct.impl.RedisDoubleLinkedList;

/**
 * @ClassName: Lindex
 * @Description: 返回列表 key 里索引 index 位置存储的元素
 * @Date: 2022/6/11 15:18
 * @Author: isheihei
 */
public class Lindex implements Command {

    private BytesWrapper key;

    private int index;

    private Resp[] array;

    @Override
    public CommandType type() {
        return CommandType.lindex;
    }

    @Override
    public void setContent(Resp[] array) {
        this.array = array;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RedisClient redisClient) {
        if ((key = getBytesWrapper(ctx, array, 1)) == null) {
            return;
        }
        BytesWrapper indexBytes;
        if ((indexBytes = getBytesWrapper(ctx, array, 2)) == null) {
            return;
        }
        try {
            index = Integer.parseInt(indexBytes.toUtf8String());
        } catch (NumberFormatException e) {
            LOGGER.error("参数无法转换为数字", e);
            ctx.writeAndFlush(new Errors(ErrorsConsts.VALUE_IS_NOT_INT));
            return;
        }

        RedisObject redisObject = redisClient.getDb().get(key);
        if (redisObject == null) {
            ctx.writeAndFlush(BulkString.NullBulkString);
            return;
        }
        if (redisObject instanceof RedisListObject) {
            RedisDataStruct data = redisObject.data();
            if (data instanceof RedisDoubleLinkedList) {
                RedisDoubleLinkedList list = (RedisDoubleLinkedList) data;
                BytesWrapper value = list.index(index);
                if (value == null) {
                    ctx.writeAndFlush(BulkString.NullBulkString);
                } else {
                    ctx.writeAndFlush(new BulkString(value));
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            ctx.writeAndFlush(new Errors(ErrorsConsts.WRONG_TYPE_OPERATION));
        }
    }
}
