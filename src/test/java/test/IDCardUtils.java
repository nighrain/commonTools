package test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import win.tools.time.DateTools;


//---------------------
//Java 身份证验证 判断性别 生日工具类
//作者：悠槿璃
//来源：CSDN
//原文：https://blog.csdn.net/qq_27455063/article/details/79551847
//版权声明：本文为博主原创文章，转载请附上博文链接！
public abstract class IDCardUtils {

    public static void main(String[] args) {
        System.out.println(isLeAgeByCnid("123456199001011234", 29));
        System.out.println(isLeAgeByCnid("123456199001011234", 30));
        System.out.println(isAgeRangeByCnid("123456199001011234", 20, 35));
    }
    // private final static Logger LOGGER = LoggerFactory.getLogger(IDCardUtils.class);

    private static final String REGPEX_18_BIT = "\\d{17}([0-9]|X)";
    private static final String REGPEX_15_BIT = "\\d{15}";

    private static final String BIRTHDAY_18_BIT = "^((19|20)\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
    private static final String BIRTHDAY_15_BIT = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";

    private static final char[] CHINA_ID_CARD_VALIDATE_BIT = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    public static boolean validateIdCard(String idCard) {
        int idLen = idCard.length();
        boolean result = false;
        if (idLen == 15) {
            result = validateFormate(idCard) && validateBirthday(idCard);
        }
        if (idLen == 18) {
            result = validateFormate(idCard) && validateBirthday(idCard) && validateLastBit(idCard);
        }
        return result;
    }

    public static boolean validateIdCardOf18(String idCard) {
        int idLen = idCard.length();
        boolean result = false;
        if (idLen == 18) {
            result = validateFormate(idCard) && validateBirthday(idCard) && validateLastBit(idCard);
        }
        return result;
    }

    // check the date if it is valid
    private static boolean checkDate(String birthday) {
        String syear = "1900";
        String smonth = "01";
        String sday = "01";
        if (birthday.length() == 6) {
            syear = "19" + birthday.substring(0, 2);
            smonth = birthday.substring(2, 4);
            sday = birthday.substring(4, 6);
        }
        if (birthday.length() == 8) {
            syear = birthday.substring(0, 4);
            smonth = birthday.substring(4, 6);
            sday = birthday.substring(6, 8);
        }
        int year = Integer.parseInt(syear);
        int month = Integer.parseInt(smonth.substring(0, 1).equals("0") ? smonth.substring(1, 2) : smonth);
        int day = Integer.parseInt(sday.substring(0, 1).equals("0") ? sday.substring(1, 2) : sday);
        boolean result = false;
        if (year >= 1900 && year < Calendar.getInstance().get(Calendar.YEAR)) {
            if (month >= 1 && month <= 12) {
                if (day >= 1 && day <= getDaysOfMonth(year, month)) {
                    result = true;
                }
            }
        }
        return result;
    }

    private static boolean validateFormate(String idCard) {
        return Pattern.matches(REGPEX_18_BIT, idCard) || Pattern.matches(REGPEX_15_BIT, idCard);
    }

    private static boolean validateLastBit(String idCard) {
        char validateBit = idCard.charAt(idCard.length() - 1);
        int validateVale = getValidateValue(idCard);
        return CHINA_ID_CARD_VALIDATE_BIT[validateVale] == validateBit;
    }

    private static int[] getEveryBitWeightValue() {
        int[] weightValueArray = new int[18];
        for (int i = 18, j = 0; i >= 1; i--, j++) {
            weightValueArray[j] = (int) (Math.pow(2, i - 1) % 11);
        }
        return weightValueArray;
    }

    private static int getValidateValue(String idcard) {
        char[] bits = idcard.toCharArray();
        int result = 0;
        int[] weigthValueArray = getEveryBitWeightValue();
        for (int i = 0, j = idcard.length() - 1; i < j; i++) {
            result += Integer.parseInt(bits[i] + "") * weigthValueArray[i];
        }
        return result % 11;
    }

    // validate birthday
    private static boolean validateBirthday(String idcard) {
        int bitSize = idcard.length();
        boolean result = false;
        if (bitSize == 15) {
            String birthday = idcard.substring(6, 12);
            if (Pattern.matches(BIRTHDAY_15_BIT, birthday)) {
                result = checkDate(birthday);
            }
        }
        if (bitSize == 18) {
            String birthday = idcard.substring(6, 14);
            if (Pattern.matches(BIRTHDAY_18_BIT, birthday)) {
                result = checkDate(birthday);
            }
        }
        return result;
    }

    private static int getDaysOfMonth(int iYear, int iMnonth) {
        Calendar cal = new GregorianCalendar(iYear, iMnonth - 1, 1);
        // Get the number of days in that month
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Check the age whether lower than specify age. The precondition is that the cnid is valid.
     * 检查年龄是否低于指定年龄。前提是cnid是有效的。
     *
     * @return
     */
    public static boolean isLeAgeByCnid(String cnid, int age) {
        String birthDayStr = null;
        if (cnid.length() == 15) {
            birthDayStr = "19" + cnid.substring(6, 12);
        } else {
            birthDayStr = cnid.substring(6, 14);
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -age);
        Date birthDay = DateTools.parse(birthDayStr, "yyyyMMdd");
        return birthDay.before(c.getTime());
//        return DateTools.isAfter(birthDay, c.getTime());

    }

    /**
     * 根据身份证号验证年龄是否在minAge与maxAge之间
     *
     * @param cnid
     * @param minAge
     * @param maxAge
     * @return
     */
    public static boolean isAgeRangeByCnid(String cnid, int minAge, int maxAge) {
        String birthDayStr = null;
        if (cnid.length() == 15) {
            birthDayStr = "19" + cnid.substring(6, 12);
        } else {
            birthDayStr = cnid.substring(6, 14);
        }
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.YEAR, -minAge);
        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.YEAR, -maxAge);
        Date birthDay = DateTools.parse(birthDayStr, "yyyyMMdd");
        return birthDay.after(startCal.getTime()) && birthDay.before(endCal.getTime());
//        return DateTools.isAfter(birthDay, startCal.getTime()) && DateTools.isBefore(birthDay, endCal.getTime());
    }

    /**
     * 检查身份证的长度是否合法
     *
     * @param cnid
     * @return
     */
    public static boolean isLengthValid(String cnid) {
        int len = cnid.length();
        if (len == 15 || len == 18) {
            return true;
        }
        return false;
    }

    public static String getGenderWithCnid(String cnid, String male, String female) {
        if (StringUtils.isNotBlank(cnid)) {
            int len = cnid.length();
            switch (len) {
                case 18:
                    return isNumEven(cnid.charAt(len - 2)) ? female : male;
                case 15:
                    return isNumEven(cnid.charAt(len - 1)) ? female : male;
                default:
                    break;
            }
        }
        return null;
    }

    private static boolean isNumEven(int num) {
        return (num & 1) == 0;
    }

    public static Integer getAgeWithCnid(String cnid) {
        Date birthday = getBirthdayWithCnid(cnid, DateTools.PATTERN_NUM_YMD);
        if (birthday != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int yearCur = cal.get(Calendar.YEAR);
            int monthCur = cal.get(Calendar.MONTH);
            int dayCur = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(birthday);
            int yearBir = cal.get(Calendar.YEAR);
            int monthBir = cal.get(Calendar.MONTH);
            int dayBir = cal.get(Calendar.DAY_OF_MONTH);

            return (yearCur - yearBir - ((monthCur > monthBir) || (monthCur == monthBir && dayCur >= dayBir) ? 0 : 1));
        }

        return null;
    }

    public static Date getBirthdayWithCnid(String cnid, String format) {
        String birthdayStr = parseBirthday(cnid);
        if (StringUtils.isNotBlank(birthdayStr)) {
            return DateTools.parse(birthdayStr, format);
        }
        return null;
    }

    public static String parseBirthday(String cnid) {
        cnid = cnid.trim();
        String birthdayStr = null;

        switch (cnid.length()) {
            case 18:
                birthdayStr = cnid.substring(6, 14);
                break;

            case 15:
                birthdayStr = "19" + cnid.substring(6, 12);
                break;
        }
        return birthdayStr;
    }
}
