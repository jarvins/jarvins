package com.jarvins.backtrack;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * N皇后问题源于8皇后问题：
 * 在 8×8 格的国际象棋上摆放八个皇后，使其不能互相攻击，即任意
 * 两个皇后都不能处于同一行、同一列或同一斜线上，问有多少种摆法。
 * <p>
 * 关键:
 * 每次选择向上回溯的时候，当前第i行棋子需要"复位"
 * 当前棋子在最后一位且不满足条件，向上回溯的时候，要循环回溯，因为上一排棋子可能也处在最后一位，退出条件就是回溯到第一排发现棋子也在第8位(或者直接回溯到第0行，退出)
 * <p>
 * example:
 * private final static int QUQUES_COUNT = 8;
 */
public class NQueuesProblem {

    /**
     * 简单的实现过程：
     * 每次让第i拍棋子从1号位置发到第queuesCount位，判断能否放下
     *
     * @param queuesCount
     * @return
     */
    public static int[] solve_NQueue(int queuesCount) {

        //记录当前旗子的位置为[i,position[i]]
        int[] position = new int[queuesCount + 1];
        //记录方案数
        int count = 0;
        //记录一个可行的方案
        int[] one = new int[queuesCount];
        //记录下当前棋子放置的下标
        int index = 1;
        for (int j = 1; j <= queuesCount; j++) {

            boolean flag = true;
            for (int i = 1; i < index; i++) {

                //第j枚棋子的位置为[i,position[i]]
                //当前这枚棋子的位置为[index,j]
                if (position[i] == j || index - i == Math.abs(position[i] - j)) {
                    flag = false;
                    break;
                }
            }

            //可以放置
            if (flag) {
                //且已经放置完了queuesCount个棋子，此时是一种放置方案
                if (index == queuesCount) {
                    position[index] = j;
                    count++;
                    System.arraycopy(position,1,one,0,queuesCount);
                    if (j == queuesCount) {
                        index--;
                        j = position[index];
                    }
                }

                //可以放置，没放完所有棋子，放下一排
                else {
                    position[index] = j;
                    index++;
                    j = 0;
                }
            }

            //不可以放置
            else {
                //向后查找
                if (j < queuesCount) {
                    continue;
                }
                //当前第index枚棋子没可行方案了，向前回溯
                while (j == queuesCount || position[index] == queuesCount) {
                    index--;
                    j = 0;
                }
                //回溯完了所有方案，退出
                if (index == 0) {
                    position[0] = count;
                    break;
                } else {
                    j = position[index];
                }
            }
        }
        //可行方案复制
        System.arraycopy(one, 0, position, 1, queuesCount);
        return position;
    }


    /**
     * 一个更好看的实现过程
     * @param queuesCount
     * @return
     */
    public static int[] solve_NQueue_better(int queuesCount){

        //记录当前旗子的位置为[i,position[i]]
        int[] position = new int[queuesCount + 1];
        //记录总放置方案数
        int count = 0;

        //记录一个可行的方案
        int[] one = new int[queuesCount];

        //表示当前放置第index枚棋子
        int index = 1;
        //初始让第一个棋子位置为[1,1]
        position[index] = 1;

        while(true){
            boolean flag =true;
            //判断当前位置的棋子是否符合条件
            for (int i = 1; i < index; i++) {
                //由于未初始化，棋子默认的位置是0，需要剔除
                if(position[index] == 0 || position[i] == position[index] || index - i == Math.abs(position[i] - position[index])){
                    flag = false;
                    break;
                }
            }
            //满足条件,没放完，下一层
            if(flag && index < queuesCount){
                index++;
                continue;
            }
            //满足条件,放完了，这是一个答案，记录
            if(flag && index == queuesCount){
                count++;
                System.arraycopy(position,1,one,0,queuesCount);
            }

            //回溯
            while (position[index] == queuesCount){
                position[index] = 1;
                index--;
            }
            //遍历完了所有可能情况，退出
            if(index == 0){
                position[0] = count;
                System.arraycopy(one,0,position,1,queuesCount);
                break;
            }
            else {
                position[index]++;
            }
        }
        return position;
    }

    public static List<List<String>> solve_standard_backtrack(int n) {
        char[][] board = new char[n][n];
        List<List<String>> result = new ArrayList<>();
        for (char[] strings : board) {
            Arrays.fill(strings, '.');
        }
        int[] position = new int[n];
        Arrays.fill(position, -1);
        backtrack(0, position, board, result);
        return result;
    }

    private static void backtrack(int row, int[] position, char[][] board, List<List<String>> result) {

        for (int i = 0; i < position.length; i++) {
            //当前可以放
            if (check(row, i, position)) {
                board[row][i] = 'Q';
                position[row] = i;

                //当前是最后一排,一个结果
                if (row == position.length - 1) {
                    List<java.lang.String> res = new ArrayList<>();
                    for (char[] chars : board) {
                        StringBuilder builder = new StringBuilder();
                        for (char aChar : chars) {
                            builder.append(aChar);
                        }
                        res.add(builder.toString());
                    }
                    result.add(res);
                }

                //下一层
                else {
                    backtrack(row + 1, position, board, result);
                }

                //恢复
                board[row][i] = '.';
                position[row] = -1;
            }
        }
    }

    private static boolean check(int i, int j, int[] position) {
        for (int k = 0; k < position.length; k++) {
            if (position[k] == -1) {
                return true;
            }
            if (position[k] == j || Math.abs(i - k) == Math.abs(j - position[k])) {
                return false;
            }
        }
        return true;
    }

}
