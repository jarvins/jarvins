package com.jarvins.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * TrieTree:
 * 字典树,又称单词查找树，Trie树，是一种树形结构，是一种哈希树的变种。
 * 典型应用是用于统计，排序和保存大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计。
 * 它的优点是：利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。
 * <p>
 * 比如对于如下几个单词: app, apple, her, brother, father, son
 * 可构建如下字典树(这里转换一下表示法,便于显示):
 * <p>
 * 正序字典树:
 *         ┌──── a ─── p ─── p ─── END ─── l ─── e
 * root ───┼──── b ─── r ─── o ─── t ─── h ─── e ─── r
 *         ├──── f ─── a ─── t ─── h ─── e ─── r
 *         └──── s ─── o ─── n
 * <p>
 * 倒序字典树:
 *         ┌──── n ─── o ─── s ─── END
 * root ───┼──── p ─── p ─── a ─── END
 *         ├──── e ─── l ─── p ─── p ─── a ─── END
 *         ├──── r ─── e ─── h ─── END ─── t ─── a ─── f ─── END
 *         └──── o ─── r ─── b ─── END
 */
public class TrieTree_Map {

    private Map<Character, TrieTree_Map> next;
    private boolean isEnd;

    public TrieTree_Map() {
        this.next = new HashMap<>();
        this.isEnd = false;
    }

    public Map<Character, TrieTree_Map> getNext() {
        return next;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void put(String str, boolean inOrder) {
        TrieTree_Map root = this;
        char[] chars = str.toCharArray();
        if(inOrder) {
            int i = 0;
            while (i < chars.length && root.next.containsKey(chars[i])) {
                root = root.next.get(chars[i++]);
            }

            while (i < chars.length) {
                char key = chars[i++];
                root.next.put(key, new TrieTree_Map());
                root = root.next.get(key);
            }
            root.isEnd = true;
        }
        else{
            int i = chars.length-1;
            while (i >= 0 && root.next.containsKey(chars[i])) {
                root = root.next.get(chars[i--]);
            }

            while (i >= 0) {
                char key = chars[i--];
                root.next.put(key, new TrieTree_Map());
                root = root.next.get(key);
            }
            root.isEnd = true;
        }
    }
}
