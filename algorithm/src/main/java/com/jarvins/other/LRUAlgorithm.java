package com.jarvins.other;

import java.util.Arrays;
import java.util.Objects;

/**
 * LRU: Least Recently Used(最近最久未被使用)
 * 它是基于计算机操作系统一个著名理论:
 * 最近使用的页面数据会在未来一段时期内仍然被使用,已经很久没有使用的页面很有可能在未来较长的一段时间内仍然不会被使用。
 * 这种思想大量被使用在缓存淘汰机制中
 */
public class LRUAlgorithm<E> {

    private static Object[] table;

    private int size;

    public LRUAlgorithm(int capacity) {
        table = new Object[capacity];
    }

    public E add(E e) {
        if (size < table.length) {
            table[size++] = e;
            return null;
        } else {
            @SuppressWarnings("unchecked")
            E o = (E) table[0];
            System.arraycopy(table, 1, table, 0, size - 1);
            table[size - 1] = e;
            return o;
        }
    }

    public boolean remove(E e) {
        boolean removed = false;
        while (true) {
            if (contains(e)) {
                int index = indexOf(e);
                if (index == size - 1) {
                    table[index] = null;
                    size--;
                } else {
                    System.arraycopy(table, index + 1, table, index, --size - index);
                    table[size] = null;
                }
                removed = true;
            } else {
                break;
            }

        }
        return removed;
    }

    public E get(E e) {
        if (contains(e)) {
            int index = indexOf(e);
            if (index < size - 1) {
                System.arraycopy(table, index + 1, table, index, size - index - 1);
                table[size - 1] = e;
            }
        }
        return e;
    }

    public void printf() {
        Object[] result = new Object[size];
        System.arraycopy(table, 0, result, 0, size);
        System.out.println(Arrays.toString(result));
    }

    private boolean contains(E e) {
        return indexOf(e) > -1;
    }

    private int indexOf(E e) {
        Objects.requireNonNull(e);
        for (int i = 0; i < size; i++) {
            if (e.equals(table[i])) {
                return i;
            }
        }
        return -1;
    }
}
