package jdk8;

import jdk8.lambda.Trader;
import jdk8.lambda.Transaction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-09-26--15:15]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p>
 * 参考
 * 版权声明：本文为CSDN博主「蒙牛真好」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/nullbull/article/details/81234250
 * </p>
 *  
 */
public class Test1Lambda {
/*
1 找出2011年发生的所有交易，并按交易额排序
2 交易员在哪些不同的城市工作过
3 查找所有来自剑桥的交易员，并按姓名排序
4 返回所有交易员的姓名字符串，并按字母顺序排序
5 有没有交易员在米兰工作的？
6 打印生活在剑桥的交易员的所有交易额
7 所有交易中，最高的交易额是多少
8 找到交易额最小的交易
 */

    private Trader raoul = new Trader("Raoul拉乌尔", "Cambridge剑桥");
    private Trader mario = new Trader("Mario马里奥", "Milan米兰");
    private Trader alan = new Trader("Alan艾伦", "Cambridge剑桥");
    private Trader brian = new Trader("Brian布莱恩", "Cambridge剑桥");

    private List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );


    //1 找出2011年发生的所有交易，并按交易额排序
    @Test
    public void lambda1() {
        List<Object> collect = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());

    }

//2 交易员在哪些不同的城市工作过
//3 查找所有来自剑桥的交易员，并按姓名排序
//4 返回所有交易员的姓名字符串，并按字母顺序排序
//5 有没有交易员在米兰工作的？
//6 打印生活在剑桥的交易员的所有交易额
//7 所有交易中，最高的交易额是多少
//8 找到交易额最小的交易


}
