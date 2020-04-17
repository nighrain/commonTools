package jdk8.lambda;

public class Transaction{
 
    private Trader trader;
    private int year;
    private int value;
 
    public Transaction(Trader trader, int year, int value)
    {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
 
    public Trader getTrader(){
        return this.trader;
    }
 
    public int getYear(){
        return this.year;
    }
 
    public int getValue(){
        return this.value;
    }
 
    public String toString(){
        return "{" + this.trader + ", " +
                "year: "+this.year+", " +
                "value:" + this.value +"}";
    }
}
//————————————————
//版权声明：本文为CSDN博主「蒙牛真好」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/nullbull/article/details/81234250