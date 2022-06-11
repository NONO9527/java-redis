package org.isheihei.redis.core.struct.impl;

import org.isheihei.redis.core.struct.RedisDataStruct;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @ClassName: RedisDoubleLinkedList
 * @Description: Redis双端列表数据类型
 * @Date: 2022/5/31 0:24
 * @Author: isheihei
 */
public class RedisDoubleLinkedList extends LinkedList<BytesWrapper> implements RedisDataStruct {
    public List<BytesWrapper> lrange(int start, int end) {
        // TODO 负数表示从后向前 以及返回
        return this.stream().skip(start).limit(end - start >= 0 ? end - start + 1 : 0).collect(Collectors.toList());
    }

    public void rpush(List<BytesWrapper> values) {
        values.stream().forEach(value -> this.offerLast(value));
    }

    public void lpush(List<BytesWrapper> values) {
        values.stream().forEach(value -> this.offerFirst(value));

    }

    public BytesWrapper lpop() {
        return this.pollFirst();
    }

    public BytesWrapper rpop() {
        return this.pollLast();
    }

    public int lrem(Integer count, BytesWrapper element) {
        int expectCount = count;
        if (count == 0) {
            return 0;
        }
        boolean flag = true;
        if (count < 0) {
            flag = false;
            count = -count;
        }
        ListIterator<BytesWrapper> iterator = this.listIterator();

        if (flag) {
            // 正向遍历
            while (iterator.hasNext() && count > 0) {
                if (element.equals(iterator.next())) {
                    iterator.remove();
                    count--;
                }
            }
        } else {
            //反向遍历 双指针
            while (iterator.hasNext()) {
                iterator.next();
            }
            while (iterator.hasPrevious() && count > 0) {
                if (element.equals(iterator.previous())) {
                    iterator.remove();
                    count--;
                }
            }
        }
        return Math.abs(expectCount) - count;
    }

    public boolean lset(Integer index, BytesWrapper element) {
        if (index >= 0 && index < this.size() - 1) {
            set(index, element);
            return true;
        } else {
            return false;
        }
    }

    public BytesWrapper index(int index) {
        if (Math.abs(index) > this.size() - 1) {
            return null;
        } else {
            // 分为正数索引和复数索引的情况
            return this.get((this.size() + index) % this.size());
        } 
    }
}
