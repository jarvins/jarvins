## Algorithm

### 基础

#### 一维数组
 - 空判断: ```arr == null || arr.length == 0```
 - 转化hash表: key(i),value(arr[i]),可以实现空间复杂度O(n),时间复杂度O(1)的遍历
    - 查找重复数字
		- arr[num[i]]++,统计数字出现的次数(如果数字范围不确定，此方法失效)
    - 存放字母
        - 如果明确是[a,z],则数组大小固定为26,则可转化hash表,存在: ```arr[i] = (char)('a' + i)```
		
#### 二维数组
 - 空判断: ```arr == null || arr.length == 0 || arr[0].length == 0```
 
#### 字符串
 - 扩容: 当场景涉及到字符串扩容(缩小)的时候(替换,合并,缩减)，扩容使用倒序遍历，缩小使用正序遍历，使复杂度降为O(n)
 
#### 链表
 - 反向遍历: 栈/递归
 - 链表反转: 
    ```java 
    public static ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode node = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return node;
    }
    ```
 - 顺序合并:
    ```java 
    //递归
    public ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = merge(l1.next, l2);
            return l1;
        } else {
            l2.next = merge(l1, l2.next);
            return l2;
        }
    }
    //非递归
    public ListNode merge(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(-1);
        ListNode pre = head;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                pre.next = l1;
                l1 = l1.next;
            } else {
                pre.next = l2;
                l2 = l2.next;
            }
            pre = pre.next;
        }
        pre.next = l1 == null ? l2 : l1;
        return head.next;
    }
    ```
 
#### 递归
 - 本质: 递归本质是栈结构,因此所有的递归都可以使用栈解决
 - 记忆化: 对于可能多次使用的结果,将其存储，保证自递归不会无限制触发，提高效率
 - 尾递归: 递归函数不在函数体最后(比如后面有计算等),需要额外空间保存之后的操作行为信息，额外增加了空间消耗,但尾递归的递归函数在函数体末尾，可以重用栈空间，达到节省栈空间的目的
 
#### 位运算
 - 异或
    - 交换: 异或可以保留2个数的信息特征，用于完成交换
    ```java 
    a = a ^ b;
    b = a ^ b;
    a = a ^ b;
    ```
    - 特性: 
        - a ^ a = 0; a ^ 0 = a, eg:查找数组唯一不重复2次的数字
        - 1 ^ 2 ^ 3 ^ ..... ^ N = 1 (N % 4 == 1 || N % 4 == 2)
        - 1 ^ 2 ^ 3 ^ ..... ^ N = 0 (N % 4 == 3 || N % 4 == 0)
        
 - 且
    n & (n-1): 将n最右边的第一个1变为0

#### 二叉树
 - 遍历
    - 递归遍历
        - 先序遍历: preOrder[0] = root, preOrder[rootIndex + 1] = root.left(如果存在,不存在即为右子), preOrder[rootIndex + leftLength + 1] = root.right(如果存在)
        - 中序遍历: leftTree(如果存在) = [0,rootIndex - 1], rightTree(如果存在) = [rootIndex + 1, inOrder.length - 1]
        - 后续遍历: postOrder[postOrder.length - 1] = root, postOrder[postOrder.length - 2] = root.right(如果存在，不存在即为左子), post[postOrder.length - rightLength - 2] = root.left(如果存在)
        - 反中序遍历: right -> node -> left, eg:[com.jarvins.tree.GreaterTree]
    - 非递归遍历(循环):
        - 利用栈 + 双层while循环实现, eg:[com.jarvins.tree.PostOrderTraversal]
        - 考虑dfs非递归遍历方式,为了实现各种序列的输出顺序，调整队列和结果list的添加顺序即可达到目的
        
#### 遍历算法
 - 广度优先算法:
    ```java 
    private List<List<TreeNode>> bfs(TreeNode node) {
        if (node == null) return null;
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<TreeNode>> lists = new ArrayList<>();
        queue.offer(node);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<TreeNode> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
                list.add(poll);
            }
            lists.add(list);
        }
        return lists;
    }
    ```
 - 深度优先算法:
    ```java 
   //递归
   private void dfs(TreeNode node, List<TreeNode> list) {
        if (node == null) return;
        list.add(node);
        dfs(node.left, list);
        dfs(node.right, list);
   }
   //非递归
    private List<TreeNode> dfs(TreeNode node) {
        List<TreeNode> list = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();
        stack.add(node);
        while (!stack.isEmpty()) {
            TreeNode poll = stack.poll();
            list.add(poll);
            if (poll.right != null) {
                stack.push(poll.right);
            }
            if (poll.left != null) {
                stack.push(poll.left);
            }
        }
        return list;
    }
    ```

#### 栈
 - 队列: 2个栈一个用于pop(),一个用于push(),每次pop栈空时将push栈压入pop栈
 - 单调栈: 每次保证栈为一个单调序列，如果不满足，依次出栈，直到满足为止(或为空)，将当前元素放入栈中
    - 单次最大差值问题: 任意序列数组，满足j > i,求解max(arr[j] - arr[i])(效率比一次遍历低)
    - 多次最大差值问题: 序列所有正向最大差值之和(可以贪心解决,存在arr[i] > arr[i-1]就并入结果)
    
#### 队列
 - 栈: 每次先插入一个空队列,将非空队列poll()到空队列,每次add()空队列,每次poll()非空队列

#### 查找
 - 顺序查找: 复杂度O(N)
 - 二分查找: 复杂度O(logN),部分排序,分段排序(如旋转数组中查找最小值[No_10_MinInRotateArray])
    - 技巧: 分段需要考虑一个问题: 如果arr[mid] < target,此时需要从mid开始还是mid+1开始，如果arr[mid] > target,此时需要从mid开始还是mid-1开始,
    比如查找有序数组插入下标,arr[mid] < target,下一个可选下标是mid+1,而如果arr[mid] > target,下一个可选下标是是mid，而不是mid-1)
 - 哈希表查找: 
 - 二叉排序树查找:

#### 动态规划
 - 状态: 选或不选,用或不用(涉及到dp[i]和dp[i+1]而且只能取其一的场景,亦或是多个场景取一个),使用状态dp[],如: dp[i][0]表示不选的最大收益,dp[i][1]表示选的最大收益,eg: [com.jarvins.dp.Rob]
 - 无后效性: 当dp的状态转义方程受多个参数影响时,当前节点最优解无法成为后续最优解的子状态,此时只能选择倒序动态规划,eg: [com.jarvins.DungeonGame]
 - 空间压缩: 若dp过程中dp[i][j]的结果只受某一行(一般是dp[i-1])的影响，则可使用空间压缩思想,将二维空间压缩成一维空间解决(这里需要在意压缩后遍的顺序,保证每次遍历结果不会直接覆盖下一次遍历使用的值)

### 进阶

#### 字典树
 - 定义: 单词查找树，Trie树，典型应用是用于统计，排序和保存大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计。
 - 优点：利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。
 
