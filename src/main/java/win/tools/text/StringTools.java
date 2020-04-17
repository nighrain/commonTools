package win.tools.text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *    
 * Title         [常用字符串工具类]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-18--15:16]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class StringTools {
    public static void main(String[] args) {
        System.out.println(isEmpty(null));
        System.out.println(isEmpty(" "));
        System.out.println(isEmpty("1 "));
    }

    /**
     * @param len 定长
     * @param input 输入的字符串
     * @return 给输入的字符串拼接空格成定长
     */
    public static String toFixedLength(int len, String input){
        if(input.length() >= len) return input;
        int sub = len - input.length();
        char[] chars = new char[sub];
        Arrays.fill(chars, ' ');
        return input + new String(chars);
    }

    public static boolean isEmpty(String str){
        return (str == null || "".equals(str.trim()));
    }

    /**
     * @param str abcDef => ABC_DEF
     * @return 驼峰转下划线的方法
     */
    public static String hump2Underline(String str) {
        if (null == str || "".equals(str.trim())) return "";
        StringBuffer sb = new StringBuffer();
        //循环每个字符转换成大写
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
            if (c >= 'A' && c <= 'Z') {
                sb.append("_" + c);
            } else if (c >= 'a' && c <= 'z') {
                sb.append((char) (c - 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param str ABC_DEF => abcDef
     * @return 下划线转驼峰的方法
     */
    public static String underline2Hump(String str) {
        if (null == str || "".equals(str.trim())) {
            return "";
        }
        String[] split = str.split("_");
        if (split.length <= 1) {
            //不包含下划线则返回全部小写
            return str.toLowerCase();
        }
        StringBuffer sb = new StringBuffer();
        //第一段全小写
        sb.append(split[0].toLowerCase());
        //余下的循环遍历首字母大写
        for (int i = 1; i < split.length; i++) {
            char c = split[i].charAt(0);
            sb.append(c + split[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }


}
