package com.jarvins.tree;

/**
 * @see TrieTree_Map
 * 考虑到每个节点的最多只有26个分支(A,Z)
 * 可以设定固定数组来保存,arr[i] 表示'a' + i;
 * 实现复杂度比map低很多
 */
public class TrieTree_Array {

    boolean isEnd;
    TrieTree_Array[] next;

    public TrieTree_Array() {
        this.isEnd = false;
        this.next = new TrieTree_Array[26];
    }

    public void put(String str, boolean inOrder) {
        TrieTree_Array tree = this;
        char[] chars = str.toCharArray();
        if (inOrder) {
            for (int i = 0; i < chars.length; i++) {
                if (tree.next[chars[i] - 'a'] == null) {
                    tree.next[chars[i] - 'a'] = new TrieTree_Array();
                }
                tree =  tree.next[chars[i] - 'a'];
            }
            tree.isEnd = true;
        }
        else{
            for (int i = chars.length - 1; i >= 0; i--) {
                if (tree.next[chars[i] - 'a'] == null) {
                    tree.next[chars[i] - 'a'] = new TrieTree_Array();
                }
                tree =  tree.next[chars[i] - 'a'];
            }
            tree.isEnd = true;
        }
    }
}
