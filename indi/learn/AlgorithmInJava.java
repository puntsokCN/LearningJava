package indi.learn;

import com.sun.org.apache.xerces.internal.xs.ItemPSVI;

import java.util.Arrays;

import static indi.learn.Common.inits;

/**
 * <h2>公共数据</h2>
 */
class Common {
    /**
     * 需要排序的对象
     */
    public final static int[] inits = {1, 3, 2, 8, 7, 6, 5, 4}; //使用时拷贝数据而不是拷贝引用
//    public final static int[] inits = {1, 2, 3, 4, 5, 6, 7, 8}; //使用时拷贝数据而不是拷贝引用
}

/**
 * <h2>冒泡排序实验</h2>
 * 是稳定排序
 */
class BubbleSort {

    public static void show() {
        System.out.println(">>> （1）冒泡排序");
        int[] target = inits.clone();   // 拷贝一下数据，为了不对公共数据做直接的修改
        int temp;                       // 中间的临时变量
        int numOut = 0;                    // 记录执行次数
        int numIn = 0;
        System.out.println("冒泡排序前：" + Arrays.toString(target));
        for (int i = 0; i < target.length - 1; i++) {       // 这层循环： 排序的 趟数
            for (int j = 0; j < target.length - i - 1; j++) {   // 这层循环： 当趟 排序时需要比较的次数
                if (target[j] > target[j + 1]) {
                    temp = target[j];
                    target[j] = target[j + 1];
                    target[j + 1] = temp;
                    numIn += 1;
                }
            }
            numOut += 1;
        }
        System.out.println("冒泡排序后：" + Arrays.toString(target));
        System.out.printf("经过 %s 趟排序\n", numOut);
        System.out.printf("总共发生了 %s 次移动操作\n", numIn);
        System.out.println();
    }

    public static void optimization() {
        // 冒泡排序优化
        // 每趟排序的⽬的就是将当前趟最⼤的数置换到对应的位置上，没有发⽣置换说明就已经排好序了。
        int[] target = inits.clone();   // 拷贝数据，为了不对公共数据做直接的修改
        int isChanged;
        int temp;                       // 中间的临时变量
        int numOut = 0;                 // 记录外层循环次数（趟数）
        int numIn = 0;                  // 记录每趟移动次数
        System.out.println(">>> （1.1）冒泡排序优化");
        System.out.println("冒泡排序前：" + Arrays.toString(target));
        for (int i = 0; i < target.length - 1; i++) {         // 这层循环： 排序的 趟数
            isChanged = 0;  // 每趟工作前，标记为 0
            for (int j = 0; j < target.length - i - 1; j++) {     // 这层循环： 当趟 排序时需要比较的次数
                if (target[j] > target[j + 1]) {
                    isChanged = 1;                          // 当进入条件，说明发生了置换，并标记为 1
                    temp = target[j];
                    target[j] = target[j + 1];
                    target[j + 1] = temp;
                    numIn += 1;
                }
            }
            // 当⽐较完这⼀趟如果发现没有发⽣置换，那么说明当前序列已经被排好序了，不需要再执⾏下去了，做无用功
            if (isChanged == 0) {
                break;
            }
            numOut += 1;
        }
        System.out.println("冒泡排序后：" + Arrays.toString(target));
        System.out.printf("经过 %s 趟排序\n", numOut);
        System.out.printf("总共发生了 %s 次移动操作\n", numIn);
    }
}

/**
 * <h2>选择排序实验</h2>
 * <p>是 不稳定 排序  -- 只有当在“⼆次”排序时不想破坏原先次序，稳定性才有意义。</p>
 * <p>它的⼯作原理是每⼀次从待排序的数据元
 * 素中选出最⼩(或最⼤)的⼀个元素，存放在序列的起始(末尾)位置，直到全部待排序的数据元素排
 * 完。<p/>
 */
class SelectSort {
    public static void show() {
        int[] target = inits.clone();   // 拷贝数据，为了不对公共数据做直接的修改
        System.out.println("\n>>> （2）选择排序");
        System.out.println("选择排序前：" + Arrays.toString(target));
        // 记录外层循环次数
        int numOut = 0;
        // 记录查询元素的总次数
        int numIn = 0;
        //记录当前趟数的最⼤值的⻆标
        int indexOfMax;
        //交换的变量
        int temp;
        //外层循环控制需要排序的趟数
        for (int i = 0; i < target.length - 1; i++) {
            //新的趟数、将⻆标重新赋值为0
            indexOfMax = 0;
            //内层循环控制遍历数组的个数并得到最⼤数的⻆标
            for (int j = 0; j < target.length - i; j++) {
                if (target[j] > target[indexOfMax]) {
                    indexOfMax = j;
                    numIn += 1;
                }
            }
            // 将此趟的最大值和最后一个数据(length-1-i)进行互换
            temp = target[indexOfMax];
            target[indexOfMax] = target[target.length - 1 - i];
            target[target.length - 1 - i] = temp;
            numOut += 1;
        }
        System.out.println("选择排序后：" + Arrays.toString(target));
        System.out.printf("总共发生了 %s 次移动操作\n", numOut);
        System.out.printf("总共发生了 %s 次查询\n", numIn);

    }
}

/**
 * <h2>插入排序</h2>
 * <p> 是稳定排序 </p>
 * <p> 在未知道数组元素的情况下，我们只能把数组的第⼀个元素作为已经排好序的有序数据，</p>
 * <p> 并将剩下的元素逐次逐个在 已排序序列中 从右往左比较，并插入其中，</p>
 */
class InsertSort {
    public static void show() {
        // 记录循环次数
        int numOut = 0;
        // 记录循环次数
        int numIn = 0;
        int[] target = inits.clone();   // 拷贝数据，为了不对公共数据做直接的修改
        System.out.println("\n>>> （3）插入排序");
        System.out.println("插入排序前：" + Arrays.toString(target));
        // 把第一个元素当作需要插入的序列目标的初值
        // 从第二个元素开始拿来插入到其中去
        for (int i = 1; i < target.length; i++) {
            int temp = target[i];  // 此次想要插入的元素
            // 前一位
            int j = i - 1;
            //如果前⼀位(已排序的数据)⽐当前数据要⼤，那么就进⼊循环⽐较（从右边开始往左比较）
            while (j >= 0 && target[j] > temp) {
                // 往后退⼀个位置，让当前数据与之前前位进⾏⽐较
                // 若比较结果是 target[j] > temp 则将前一位数据覆盖到后面的位置上。
                target[j + 1] = target[j];
                j -= 1;  //不断往前，直到退出循环
                numIn += 1; // 记录移位操作的次数
            }
            // 当退出循环说明：当前元素在序列中找到了合适的位置了(合适的 j)，则将当前数据插⼊合适的位置中
            target[j + 1] = temp;
            numOut += 1; // 记录排序中插入的次数

        }
        System.out.println("插入排序后：" + Arrays.toString(target));
        System.out.printf("排序经过 %s 次插入\n", numOut);
        System.out.printf("总共发生了 %s 次移位操作\n", numIn);
    }
}

/**
 * <h2>快速排序，通过对象调用方法</h2>
 */
class QuickSort {
    int[] target;   // 拷贝数据，为了不对公共数据做直接的修改
    int numChange;              // 记录移位操作数量
    int numRecursion;           // 记录递归次数

    /**
     * <h2>快速排序</h2>
     * <p>
     * 快速排序由C. A. R. Hoare在1962年提出。它的基本思想是：通过⼀趟排序将要排序的数据分割成
     * 独⽴的两部分，其中⼀部分的所有数据都⽐另外⼀部分的所有数据都要⼩，然后再按此⽅法对这
     * 两部分数据分别进⾏快速排序，整个排序过程可以递归进⾏，以此达到整个数据变成有序序列。
     * </p>
     * <p></p>
     * <p></p>
     */
    QuickSort(){ //保证对象间数据的独立性
        target = inits.clone();   // 拷贝数据，为了不对公共数据做直接的修改
        numChange = 0;              // 记录移位操作数量
        numRecursion = 0;           // 记录递归次数
    }

    /**
     * @param indexOfFirstElement 指向数组第⼀个元素
     * @param indexOfLastElement  指向数组最后⼀个元素
     */
    public void show(int indexOfFirstElement, int indexOfLastElement) {
        int left = indexOfFirstElement;     // 左端
        int right = indexOfLastElement;     // 右端
        int pivot = target[(indexOfFirstElement + indexOfLastElement) / 2]; // 支点元素
        //左右两端进⾏扫描，只要两端还没有交替，就⼀直扫描
        while (left <= right) {
            // 在支点左端扫描，直到寻找到比支点 大 的数
            while (pivot > target[left]) {
                left += 1;
            }
            // 在支点右端扫描，直到寻找到比支点 小 的数
            while (pivot < target[right]) {
                right -= 1;
            }
            // 此时已经分别找到了⽐⽀点⼩的数(右边)、⽐⽀点⼤的数(左边)，它们按条件进⾏交换
            if (left <= right){
                int temp = target[left];
                target[left] = target[right];
                target[right] = temp;
                left += 1;      // 继续寻找
                right -= 1;     // 继续寻找
                numChange +=1;
            }
        }
        // 上⾯⼀个while能够保证第⼀趟排序⽀点的左边⽐⽀点⼩，⽀点的右边⽐⽀点⼤了。
        // 接下来，就开始继续对左右两部分，递归地进行快速排序
        // “左边”再做排序，直到左边剩下⼀个数(递归出⼝)
        if (indexOfFirstElement < right){
            show(indexOfFirstElement, right);
            numRecursion += 1;  // 记录递归次数
        }
        //“右边”再做排序，直到右边剩下⼀个数(递归出⼝)
        if (indexOfLastElement > left) {
            show(left, indexOfLastElement);
            numRecursion += 1;  // 记录递归次数
        }

    }

    /**
     * 重载 show(int indexOfFirstElement, int indexOfLastElement) 达到默认参数效果
     * <p>* @param indexOfFirstElement = 0   指向数组第⼀个元素</p>
     * <p>* @param indexOfLastElement = inits.length   指向数组最后⼀个元素</p>
     * 格式化输出
     */
    public void show() {
        System.out.println("\n>>> （4）快速排序");
        System.out.println("快速排序前：" + Arrays.toString(target));
        show(0, inits.length - 1); // 快速排序
        System.out.println("快速排序后：" + Arrays.toString(target));
        System.out.printf("排序中总共发生了 %s 次移位操作\n", numChange);
        System.out.printf("排序中总共发生 %s 次递归\n", numRecursion);
    }
}

public class AlgorithmInJava {

    public static void main(String[] args) {

        // 1.冒泡排序实验
        BubbleSort.show();
        BubbleSort.optimization();

        // 2.选择排序实验
        SelectSort.show();

        // 3.插入排序实验
        InsertSort.show();

        // 4.快速排序
        new QuickSort().show();

        // 5.归并排序

        // 6.希尔排序

        // 7.堆排序

        // 8.基数排序（桶排序）

        // 9.链表

        // 10.栈

        // 11.队列

        // 12.二叉树

        // 13.图
    }
}
