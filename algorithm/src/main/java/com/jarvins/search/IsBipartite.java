package com.jarvins.search;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 给定一个无向图graph，当这个图为二分图时返回true。
 * <p>
 * 如果我们能将一个图的节点集合分割成两个独立的子集A和B，
 * 并使图中的每一条边的两个节点一个来自A集合，一个来自B集合，
 * 我们就将这个图称为二分图。
 * <p>
 * graph将会以邻接表方式给出，graph[i]表示图中与节点i相连的所有节点。
 * 每个节点都是一个在0到graph.length-1之间的整数。这图中没有自环和平行边： 
 * graph[i] 中不存在i，并且graph[i]中没有重复的值。
 * <p>
 * <p>
 * 示例 1:
 * 输入: [[1,3], [0,2], [1,3], [0,2]]
 * 输出: true
 * 解释:
 * 无向图如下:
 * 0----1
 * |    |
 * |    |
 * 3----2
 * 我们可以将节点分成两组: {0, 2} 和 {1, 3}。
 * <p>
 * 示例 2:
 * 输入: [[1,2,3], [0,2], [0,1,3], [0,2]]
 * 输出: false
 * 解释:
 * 无向图如下:
 * 0----1
 * | \  |
 * |  \ |
 * 3----2
 * 我们不能将节点分割成两个独立的子集。
 */
public class IsBipartite {


    private static boolean valid;

    public static void main(String[] args) {

        int[][] arrA = {{}, {2, 4, 6}, {1, 4, 8, 9}, {7, 8}, {1, 2, 8, 9}, {6, 9}, {1, 5, 7, 8, 9}, {3, 6, 9}, {2, 3, 4, 6, 9}, {2, 4, 5, 6, 7, 8}};
        int[][] arrB = {{1}, {0, 3}, {3}, {1, 2}};
        int[][] arrC = {{1, 3}, {0, 2}, {1, 3}, {0, 2}};

        System.out.println(isBipartite_dfs(arrA));
        System.out.println(isBipartite_bfs(arrA));
        System.out.println(isBipartite_dfs(arrB));
        System.out.println(isBipartite_bfs(arrB));
        System.out.println(isBipartite_dfs(arrB));
        System.out.println(isBipartite_bfs(arrC));


    }

    public static boolean isBipartite_dfs(int[][] graph) {
        valid = true;
        int[] result = new int[graph.length];
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0 && valid) {
                result[i] = -1;
                dfs(graph, result, i);
            }
        }
        return valid;
    }

    public static boolean isBipartite_bfs(int[][] graph) {
        valid = true;
        int[] result = new int[graph.length];
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                queue.add(i);
                result[i] = -1;
            }
            while (!queue.isEmpty()) {
                Integer poll = queue.poll();
                int flag = result[poll] == -1 ? 1 : -1;
                for (int l : graph[poll]) {
                    if (result[l] == 0) {
                        result[l] = flag;
                        queue.add(l);
                    } else if (result[l] != flag) {
                        return false;
                    }
                }
            }
        }
        return valid;
    }


    private static void dfs(int[][] graph, int[] result, int index) {
        int flag = result[index] == -1 ? 1 : -1;
        for (int tar : graph[index]) {
            if (result[tar] == 0) {
                result[tar] = flag;
                dfs(graph, result, tar);
                if (!valid) {
                    return;
                }
            } else {
                if (result[tar] == result[index]) {
                    valid = false;
                    return;
                }
            }
        }
    }
}
