package win.tools.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *    
 * Title         [通过正则表达式校验字符串]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-22--22:07]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 * <c>
 * 参考: - java API 1.6
 *      - java.util.regex包下的Pattern和Matcher详解（正则匹配）
 *          https://www.cnblogs.com/tongxuping/p/7832895.html
 *      - java正则表达式(java.util.regex包)
 *          https://383984216-qq-com.iteye.com/blog/1396595
 *      - Java 正则表达式
 *          https://www.runoob.com/java/java-regular-expressions.html
 *      -JAVA 正则表达式 RegexUtil
 *          https://blog.csdn.net/yanxilou/article/details/89453633
 * </c>
 */
public class RegexTool {
    public static void main(String[] args) {
//        String str = "13345678901";
//        Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
//        Matcher matcher = pattern.matcher(str);
//        System.out.println(matcher.matches());
//
//        System.out.println(Pattern.matches(REGEX_MOBILE_SIMPLE, str));
//        System.out.println("=================");
//        System.out.println(isMobileNo("10345678901"));
//        System.out.println(isMobileNo("11345678901"));
//        System.out.println(isMobileNo("12345678901"));
//        System.out.println(isMobileNo("13345678901"));
//        System.out.println(isMobileNo("14345678901"));

//        String s = "\t\t12 34\r\n\tqwer";
//        String s1 = s.replaceAll("\\s+", " ");
//        System.out.println(s1);

//        System.out.println(Pattern.matches("15[\\d&&[^4]]", "154"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "171"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "172"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "173"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "174"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "175"));
//        System.out.println(Pattern.matches("17[\\d&&[^49]]", "179"));


        /*ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> t = new ArrayList<String>();
        ArrayList<String> f = new ArrayList<String>();


        for (int j = 0; j < 10; j++) {
            for (int k = 0; k < 10; k++) {
                list.add("1"+j+k+"12345678");
            }
        }

        for (String s : list) {
            boolean b = Pattern.matches(REGEX_MOBILE_EXACT_201611, s);
            System.out.println(s+"\t"+ b);
            if(b) t.add(s);
            else f.add(s);
        }
        System.out.println(t.size()+" true "+t);
        System.out.println(f.size()+" false "+f);*/

//        System.out.println(isTel("12312345678"));
        System.out.println(isIDCard18("123456789012345678"));

    }

    public static final String REGEX_MOBILE_SIMPLE = "^1[3456789]\\d{9}$";  //同 ^1[\d&&[^012]]\d{9}$
    /******************** 正则相关常量 ********************/
    /**
     * 正则：手机号（简单）
     */
//    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 正则：手机号（精确）2016-11
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT_201611 = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则:手机号(精确) 2019-01
     * 参考：https://www.jianshu.com/p/597e3067ac7e
     * <p>移动: 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通: 130、131、132、145、155、156、166、175、176、185、186</p>
     * <p>电信: 133、149、153、173、177、180、181、189、191、199</p>
     * <p>
     *     虚拟运营商
     *     <xn>移动：1703、1705、1706、165、172、1440(物联网)</xn>
     *     <xn>联通：1704、1707、1708、1709、171、167</xn>
     *     <xn>电信：1700、1701、1702、162、1410(物联网)</xn>
     * </p>
     * <p>卫星通信: 1349(电信) 146、17400-17405(联通) 148(移动)</p>
     * <p>合并总结: 130-139 | 141 | 144-149 | 150-153 | 155-159 | 162 | 165-167 | 170-173 | 175-178 | 180-189 | 191 | 198 | 199 </p>
     * <p>反向总结(纯属娱乐): 100-109 | 110-119 | 120-129 | 140 | 142-143 | 154 | 160-161 | 163-164 | 168-169 | 174 | 179 | 190 | 192-197 </p>
     *
     */
    public static final String REGEX_MOBILE_EXACT_201901_JAVA = "^((13\\d)|(14[1,4-9])|(15[\\d&&[^4]])|(16[2,5-7])|(17[\\d&&[^49]])|(18\\d)|(19[189]))\\d{8}$";
    public static final String REGEX_MOBILE_EXACT_201901_JS = "^((13\\d)|(14[1,4-9])|(15[0-3,5-9])|(16[2,5-7])|(17[0-3,5-8])|(18\\d)|(19[189]))\\d{8}$";
    public static final String REGEX_MOBILE_EXACT_201901_FX = "^(([0,2-9]\\d{2})|((1[012][\\d])|(14[023])|(154)|(16[013489])|(17[49])|(19[0,2-7])))\\d{8}$";
    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "^\\d{3,4}[-]?\\d{7,8}$"; // "(\\d{3}-?\\d{8})|(\\d{4}-\\{7,8})"; //
    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * 正则：汉字
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

/************** 以下摘自http://tool.oschina.net/regex **************/

    /**
     * 正则：双字节字符(包括汉字在内)
     */
    public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
    /**
     * 正则：空白行
     */
    public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
    /**
     * 正则：QQ号
     */
    public static final String REGEX_TENCENT_NUM = "[1-9][0-9]{4,}";
    /**
     * 正则：中国邮政编码
     */
    public static final String REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正则：正整数
     */
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 正则：负整数
     */
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 正则：整数
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    /**
     * 正则：非负整数(正整数 + 0)
     */
    public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    /**
     * 正则：非正整数（负整数 + 0）
     */
    public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /**
     * 正则：正浮点数
     */
    public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 正则：负浮点数
     */
    public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    /************** If u want more please visit http://toutiao.com/i6231678548520731137/ **************/

/************** 以下摘自https://blog.csdn.net/yanxilou/article/details/89453633 **************/
    /**
     * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
     */
    public static final String  AGE="^(?:[1-9][0-9]?|1[01][0-9]|120)$";

    /************** end摘自https://blog.csdn.net/yanxilou/article/details/89453633 **************/

    private RegexTool() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 验证手机号（简单）
     *
     * @param input 手机号
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(CharSequence input) {
        return regular(REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 手机号
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return regular(REGEX_MOBILE_EXACT_201901_JAVA, input);
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(CharSequence input) {
        return regular(REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return regular(REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return regular(REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return regular(REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(CharSequence input) {
        return regular(REGEX_URL, input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return regular(REGEX_ZH, input);
    }

//    /**
//     * 验证用户名
//     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
//     *
//     * @param input 待验证文本
//     * @return {@code true}: 匹配<br>{@code false}: 不匹配
//     */
//    public static boolean isUsername(CharSequence input) {
//        return regular(REGEX_USERNAME, input);
//    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(CharSequence input) {
        return regular(REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(CharSequence input) {
        return regular(REGEX_IP, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean regular(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) return null;
        List<String> matches = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) return null;
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

}
