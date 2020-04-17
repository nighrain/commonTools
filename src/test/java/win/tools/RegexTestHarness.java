package win.tools;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * <c>
 *     参考:
 *      -java正则表达式(java.util.regex包)
 *          https://383984216-qq-com.iteye.com/blog/1396595
 * </c>
 */
public class RegexTestHarness {

    private static String[][] regx = {
            //              0          1                2               3               4
            new String[]{"cat",     "cat.",         "cat\\.",       "\\Qcat.\\E",   "catcatcatqcatecat."},  //0
            new String[]{"[abc]at", "[a-c]at",      "[^a-c]at",     "cat[1-5]",     "aatbatcat1dateatcat2"},//1
            new String[]{"[1-3]",   "[1-3[7-9]]",   "[0-9&&[468]]", "[0-9&&[^468]]","0123456789"},          //2 //范围 并集 交集 交集
            new String[]{"\\d",     "\\D",          "[^\\d]",       "[0-9]",        "123_qwe456asd"},       //3        //预定义字符
            new String[]{"\\w",     "\\W",          "[^\\w]",       "[a-zA-Z_0-9]", "123_qwe456asd"},       //4 //预定义字符
    };
    // 量词 quantifier
    private static String[][] quantifier = {
            //  a? 零次或一次[会零长度匹配]; a* 零次或多次[会零长度匹配]; a+ 一次或多次; a{3} 恰好3次; a{3,} 最少3次; a{3,6} 3~6次;
            new String[]{"a?", "a*", "a+",  "a" , "", "aaa"},  //
            new String[]{"a{3}",    "a{3,}",    "a{3,6}",   "aa",   "aaaa", "aaaaaaa"},

            new String[]{"dog+",    "[dog]+",   "(dog)+",   "[dog]{3}", "doggg",    "ddooogg",  "dogdog"},


            new String[]{"a??", "a*?", "a+?", "a{3}?", "a{3,}?", "a{3,6}?", "a1aa2aaa3aaaa4aaaaa5aaaaaa6aaaaaaa7"},  //
            new String[]{"a?+", "a*+", "a++", "a{3}+", "a{3,}+", "a{3,6}+", "a1aa2aaa3aaaa4aaaaa5aaaaaa6aaaaaaa7"},  //
    };

    public static void main(String[] args) {

//        Pattern pattern = Pattern.compile(regx[4][0]);
//        Matcher matcher = pattern.matcher(regx[4][4]);
//        Pattern pattern = Pattern.compile(quantifier[1][1]);
//        Matcher matcher = pattern.matcher(quantifier[1][5]);
//        Pattern pattern = Pattern.compile("[dog]{3}");
//        Matcher matcher = pattern.matcher("dogdddgodddo");
        Pattern pattern = Pattern.compile("(\\d\\d)\\1");
        Matcher matcher = pattern.matcher("1212");
        boolean found = false;

        while (matcher.find()) {
            System.out.format("I found the text [%s] starting at index [%d] and ending at index [%d].%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            found = true;
        }

        if (!found) {
            System.out.format("No match found.%n");
        }
    }

/*
分析输入字符串  xfooxxxxxxfoo
.*foo
.*?foo
.*+foo
第一个例子使用greedy量词.*搜索“任何内容”零次或者多次，后面是字母f、o、o。
因为是greedy量词，所以表达式的.*部分首先读完整个字符串。
这样，整个表达式不会成功，因为最后三个字母（“f”“o”“o”）已经被消耗了。
所以匹配器缓慢地一次后退一个字母，一直后退到最右侧出现“foo”为止，这里匹配成功并且搜索停止。

但是第二个例子使用的量词是reluctant量词，所以它首先消耗“无内容”。
因为“foo”没有出现在字符串的开头，所以迫使它消耗掉第一个字母（x），
这样就在索引0和4的位置触发第一个匹配。我们的测试示例继续处理，直到输入字符串耗尽为止。它在索引4和13找到了另一个匹配。

第三个例子找不到匹配，因为是possessive量词。
这种情况下，.*+消耗整个输入字符串，在表达式的结尾没有剩下满足“foo”的内容。
possessive量词用于处理所有内容，但是从不后退的情况；
在没有立即发现匹配的情况下，它的性能优于功能相同的greedy量词。


 */
}  