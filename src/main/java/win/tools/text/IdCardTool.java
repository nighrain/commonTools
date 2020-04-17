package win.tools.text;

import win.tools.time.DateTools;

import java.util.Date;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-06-18--17:53]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class IdCardTool {
    public static void main(String[] args) {
        System.out.println(check18("123456199001011239"));
    }
    //参考
    // https://blog.csdn.net/qq_35192224/article/details/78313737
    // https://blog.csdn.net/tianyangwww/article/details/84569193
    // https://blog.csdn.net/qq_27455063/article/details/79551847
    // https://download.csdn.net/download/guohuaiyong1101/5251141

    // https://blog.csdn.net/xjy9266/article/details/86183099

    // https://www.imooc.com/article/42293
    // js最新手机号码、电话号码正则表达式 http://caibaojian.com/regexp-example.html

    // https://blog.csdn.net/abc_email/article/details/51241628

    // 身份证号码的编码规则及校验 https://www.jianshu.com/p/ead5b08e9839
	//https://www.jianshu.com/p/ead5b08e9839


    private final static char[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'}; //校验位
    private final static int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; //加权因子  //Math.pow(2,  i - 1) % 11
    /**
     * <pre>
     * 省、直辖市代码表：
     *     11 : 北京  12 : 天津  13 : 河北   14 : 山西  15 : 内蒙古
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江 31 : 上海  32 : 江苏
     *     33 : 浙江  34 : 安徽  35 : 福建   36 : 江西  37 : 山东
     *     41 : 河南  42 : 湖北  43 : 湖南   44 : 广东  45 : 广西  46 : 海南
     *     50 : 重庆  51 : 四川  52 : 贵州   53 : 云南  54 : 西藏
     *     61 : 陕西  62 : 甘肃  63 : 青海   64 : 宁夏  65 : 新疆
     *     71 : 台湾
     *     81 : 香港  82 : 澳门
     *     91 : 国外
     * </pre>
     */
    private final static String PROVINCE_CODE[] = {"11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44",
            "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

    private final static Date MIN_BIRTH_DATE = new Date(-2209017600000L); // 身份证的最小出生日期,1900年1月1日


    public static boolean check(String idCardCode) {
        if (idCardCode == null || "".equals(idCardCode.trim())) return false;
        idCardCode = idCardCode.trim();
        if (idCardCode.length() == 18) {
            return false;
        } else if (idCardCode.length() == 15) {
            return false;
        } else {
            return false;
        }
    }

    private static boolean check18(String idCardCode) {
        if (!RegexTool.isIDCard18(idCardCode)) return false;
        //先校验长度
        if (idCardCode.length() != 18) return false;
        //字符
        if (!isNum(idCardCode.substring(0, 17))) return false;
        //最后一位字符
        char c = idCardCode.charAt(17);
        if(c != 'x' && c != 'X'){
            if(c < '0' || c > '9') return false;
        }
        //地区码
        if (!checkProvince(idCardCode.substring(0, 2))) return false;
        //出生日期
        if(checkBirthday(idCardCode.substring(6,14)) == null) return false;
        //校验位
        return true;
    }

    private static boolean checkProvince(String provinceCode) {
        for (int i = 0; i < PROVINCE_CODE.length; i++) {
            if (PROVINCE_CODE[i].equals(provinceCode)) return true;
        }
        return false;
    }

    private static Date checkBirthday(String birthdayStr){
        Date date = DateTools.parse(birthdayStr, DateTools.PATTERN_NUM_YMD);
        if(date == null) return null;
        if(date.before(MIN_BIRTH_DATE)) return null;
        return date;
    }

    private static boolean isNum(String str){
        if(null == str || "".equals(str.trim())) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c < '0'|| c > '9'){
                return false;
            }
        }
        return true;
    }


    /**
     * @param odd 是否为奇数
     * @return 奇数 true
     */
    private static boolean isNumOdd(int odd) {
        // 位运算 n&1 == 0 偶数
        return (odd & 1) != 0;
    }

    /**
     * @param even 是否为偶数
     * @return 偶数 true
     */
    private static boolean isNumEven(int even) {
        return (even & 1) == 0;
    }

    class IdCard {
        private boolean isReal;
        private String idCardCode;
        private Integer provinceCode;
        private Integer cityCode;
        private Date birthday;
        private Integer age;
        private Sex sex;
    }

    //性别
    enum Sex {//famale male isNumEven
        FAMALE,
        MALE
    }
}
