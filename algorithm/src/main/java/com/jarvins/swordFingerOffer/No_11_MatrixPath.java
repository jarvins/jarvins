package com.jarvins.swordFingerOffer;

/**
 * 请设计一个函数，用来判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。路径可以从矩阵中的任意一格开始，每一步可以在矩阵中向左、右、上、下移动一格。如果一条路径经过了矩阵的某一格，那么该路径不能再次进入该格子。例如，在下面的3×4的矩阵中包含一条字符串“bfce”的路径（路径中的字母用加粗标出）。
 * <p>
 * [["a","b","c","e"],
 * ["s","f","c","s"],
 * ["a","d","e","e"]]
 * <p>
 * 但矩阵中不包含字符串“abfb”的路径，因为字符串的第一个字符b占据了矩阵中的第一行第二个格子之后，路径不能再次进入这个格子。
 * <p>
 * 示例 1：
 * <p>
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 * 输出：true
 * 示例 2：
 * <p>
 * 输入：board = [["a","b"],["c","d"]], word = "abcd"
 * 输出：false
 */
public class No_11_MatrixPath {

    public static void main(String[] args) {
        char[][] chars = {{'c', 'a', 'a'}, {'a', 'a', 'a'}, {'b', 'c', 'd'}};
        String word = "aab";
        System.out.println(exist(chars, word));
    }

    public static boolean exist(char[][] board, String word) {
        char[] chars = word.toCharArray();
        if (board == null || board.length <= 0 || board[0].length <= 0) return false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (contains(i, j, board, chars, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean contains(int i, int j, char[][] board, char[] word, int wordIndex) {
        if (i < 0 || i > board.length - 1 || j > board[0].length - 1 || j < 0 || board[i][j] != word[wordIndex])
            return false;
        if (wordIndex == word.length - 1) return true;
        char c = board[i][j];
        board[i][j] = '\n';
        if (contains(i - 1, j, board, word, wordIndex + 1) ||
                contains(i, j - 1, board, word, wordIndex + 1) ||
                contains(i + 1, j, board, word, wordIndex + 1) ||
                contains(i, j + 1, board, word, wordIndex + 1)) {
            return true;
        }
        board[i][j] = c;
        return false;
    }
}
