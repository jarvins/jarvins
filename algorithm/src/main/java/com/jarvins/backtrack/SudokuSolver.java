package com.jarvins.backtrack;

/**
 * 编写一个程序，通过已填充的空格来解决数独问题。
 * <p>
 * 一个数独的解法需遵循如下规则：
 * <p>
 * 数字 1-9 在每一行只能出现一次。
 * 数字 1-9 在每一列只能出现一次。
 * 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 * 空白格用 '.' 表示。
 * 5 3 . . 7 . . . .
 * 6 . . 1 9 5 . . .
 * . 9 8 . . . . 6 .
 * 8 . . . 6 . . . 3
 * 4 . . 8 . 3 . . 1
 * 7 . . . 2 . . . 6
 * . 6 . . . . 2 8 .
 * . . . 4 1 9 . . 5
 * . . . . 8 . . 7 9
 * <p>
 * 一个数独。
 * <p>
 * <p>
 * 5 3 4 6 7 8 9 1 2
 * 6 7 2 1 9 5 3 4 8
 * 1 9 8 3 4 2 5 6 7
 * 8 5 9 7 6 1 4 2 3
 * 4 2 6 8 5 3 7 9 1
 * 7 1 3 9 2 4 8 5 6
 * 9 6 1 5 3 7 2 8 4
 * 2 8 7 4 1 9 6 3 5
 * 3 4 5 2 8 6 1 7 9
 */
public class SudokuSolver {

    static boolean fnished = false;

    public static void solveSudoku(char[][] board) {
        fnished = false;
        backTrack(0, 0, board);
    }

    private static void backTrack(int x, int y, char[][] board) {
        if (board[x][y] == '.') {
            for (int i = 1; i < 10; i++) {
                if (check(board, x, y, (char) (i + 48))) {
                    board[x][y] = (char) (i + 48);
                    if (y < 8) {
                        backTrack(x, y + 1, board);
                    } else if (x < 8) {
                        backTrack(x + 1, 0, board);
                    } else {
                        fnished = true;
                        return;
                    }
                    if (fnished) {
                        return;
                    }
                    board[x][y] = '.';
                }
            }
        } else {
            if (y < 8 && !fnished) {
                backTrack(x, y + 1, board);
            } else if (x < 8 && !fnished) {
                backTrack(x + 1, 0, board);
            } else {
                fnished = true;
            }
        }
    }

    private static boolean check(char[][] board, int x, int y, char c) {
        for (int i = 0; i < board.length; i++) {
            if (board[x][i] == c || board[i][y] == c) return false;
        }
        for (int i = x / 3 * 3; i < x / 3 * 3 + 3; i++) {
            for (int j = y / 3 * 3; j < y / 3 * 3 + 3; j++) {
                if (board[i][j] == c) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        char[] c1 = {'5', '3', '.', '.', '7', '.', '.', '.', '.'};
        char[] c2 = {'6', '.', '.', '1', '9', '5', '.', '.', '.'};
        char[] c3 = {'.', '9', '8', '.', '.', '.', '.', '6', '.'};
        char[] c4 = {'8', '.', '.', '.', '6', '.', '.', '.', '3'};
        char[] c5 = {'4', '.', '.', '8', '.', '3', '.', '.', '1'};
        char[] c6 = {'7', '.', '.', '.', '2', '.', '.', '.', '6'};
        char[] c7 = {'.', '6', '.', '.', '.', '.', '2', '8', '.'};
        char[] c8 = {'.', '.', '.', '4', '1', '9', '.', '.', '5'};
        char[] c9 = {'.', '.', '.', '.', '8', '.', '.', '7', '9'};

        char[][] ch = {c1, c2, c3, c4, c5, c6, c7, c8, c9};
        solveSudoku(ch);
        System.out.println();
    }

}
