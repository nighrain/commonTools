package win.tools.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *    
 * Title         [时间工具类]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-14--13:56]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p>
 *  yyyy-MM-dd'T'HH:mm:ssZZ
 * y 年 Year 1996; 96 <br>
 * M 年中的月份 Month July; Jul; 07 <br>
 * d 月份中的天数 Number 10 <br>
 * H 一天中的小时数（0-23） Number 0 <br>
 * m 小时中的分钟数 Number 30 <br>
 * s 分钟中的秒数 Number 55 <br>
 * S 毫秒数 Number 978 <br>
 * 字母	日期或时间元素	表示	示例
 * G	Era 标志符	Text	AD
 * y	年	Year	1996; 96
 * M	年中的月份	Month	July; Jul; 07
 * w	年中的周数	Number	27
 * W	月份中的周数	Number	2
 * D	年中的天数	Number	189
 * d	月份中的天数	Number	10
 * F	月份中的星期	Number	2
 * E	星期中的天数	Text	Tuesday; Tue
 * a	Am/pm 标记	Text	PM
 * H	一天中的小时数（0-23）	Number	0
 * k	一天中的小时数（1-24）	Number	24
 * K	am/pm 中的小时数（0-11）	Number	0
 * h	am/pm 中的小时数（1-12）	Number	12
 * m	小时中的分钟数	Number	30
 * s	分钟中的秒数	Number	55
 * S	毫秒数	Number	978
 * z	时区	General time zone	Pacific Standard Time; PST; GMT-08:00
 * Z	时区	RFC 822 time zone	-0800
 * ---------------------
 * 参数含义参考：https://blog.csdn.net/beauty9235/article/details/2033133
 * </p>
 */
public class DateTools {
    //   Hyphen -   slash /
    public static final String PATTERN_HYPHEN_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_HYPHEN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_SLASH_YMD_HMS = "yyyy/MM/dd HH:mm:ss";
    public static final String PATTERN_SLASH_YMD = "yyyy/MM/dd";
    public static final String PATTERN_NUM_YMD_HMS = "yyyyMMddHHmmss";
    public static final String PATTERN_NUM_YMD = "yyyyMMdd";

    private static Calendar calendar = Calendar.getInstance();

    private DateTools() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @param date 输入日期
     * @return 使用默认格式(y - M - d H : m : s), 格式化输入日期
     */
    public static String format(Date date) {
        return format(date, PATTERN_HYPHEN_YMD_HMS);
    }

    /**
     * @param date    输入日期
     * @param pattern 指定格式,若为null则使用默认格式 (y-M-d H:m:s)
     * @return 使用指定格式格式, 格式化输入日期
     */
    public static String format(Date date, String pattern) {
        String pattern_ = pattern;
        if (pattern_ == null || "".equals(pattern_.trim())) {
            pattern_ = PATTERN_HYPHEN_YMD_HMS;
        }
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern_);
        return sdf.format(date);
    }

    /**
     * @param pattern 时间格式,若为null则使用默认格式 (y-M-d H:m:s)
     * @return 当前时间的格式字符串
     */
    public static String formatNow(String pattern) {
        String pattern_ = pattern;
        if (pattern_ == null || "".equals(pattern_.trim())) {
            pattern_ = PATTERN_HYPHEN_YMD_HMS;
        }
        return format(new Date(), pattern_);
    }

    /**
     * @param dateStr 日期字符串
     * @param pattern 时间格式,若为null则使用默认格式 (y-M-d H:m:s)
     * @return 按照指定格式解析时间
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            String pattern_ = pattern;
            if (pattern_ == null) {
                pattern_ = PATTERN_HYPHEN_YMD_HMS;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern_);
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param years 要添加的年数
     * @return 指定年数后的今天
     */
    public static Date addYearsToday(int years) {
        return addYearsThatDate(new Date(), years);
    }

    /**
     * @param date  日期基准
     * @param years 要添加的年数
     * @return 日期基准, 指定年数后的该日期
     */
    public static Date addYearsThatDate(Date date, int years) {
        int years_ = years;
//        Date date_ = removeNull(date);
        if(date == null){
            return null;
        }
        Date date_ = date;
        calendar.setTime(date_);
        calendar.add(Calendar.YEAR, years_);
        String format = format(calendar.getTime(), PATTERN_HYPHEN_YMD);
        return parse(format, PATTERN_HYPHEN_YMD);
    }

    /**
     * @param date  日期基准
     * @param days 要添加的天数
     * @return 日期基准, 该日期的指定天数后的日期
     */
    public static Date addDaysThatDate(Date date, int days) {
        int days_ = days;
//        Date date_ = removeNull(date);
        if(date == null){
            return null;
        }
        Date date_ = date;
        calendar.setTime(date_);
        calendar.add(Calendar.DAY_OF_MONTH, days_);
        return calendar.getTime();
    }
//    public static Date addDaysThatDate2(Date date,int days){
//        int days_ = days;
//        Date date_ = removeNull(date);
//        long miOfDay = 1000*60*60*24;
//        long time = date_.getTime() + (days * miOfDay);
//        return new Date(time);
//    }

    /**
     * @param date        日期基准
     * @param millisecond 毫秒值
     * @return 给日期基准加上指定的毫秒值
     */
    public static Date addMsecsThatDate(Date date, long millisecond) {
//        Date date_ = removeNull(date);
        if(date == null){
            return null;
        }
        Date date_ = date;
        long time = date_.getTime() + millisecond;
        return new Date(time);
    }

    /**
     * @param date 指定日期
     * @return 指定日期的当天开始时间值
     */
    public static Date getThatDateStart(Date date) {
//        Date date_ = removeNull(date);
        if(date == null){
            return null;
        }
        Date date_ = date;
        calendar.setTime(date_);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param thatDate 指定时间
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 若指定时间在开始时间和结束时间之间,返回 true ,否则 false
     */
    public static boolean isInDateRange(Date thatDate, Date beginDate, Date endDate) {
        Date date_ = removeNull(thatDate);
        if (beginDate == null && endDate == null) return true;

        if (beginDate == null) return date_.before(endDate);
        if (endDate == null) return date_.after(beginDate);

        if (beginDate.compareTo(endDate) > 0) return false;

        if (date_.compareTo(beginDate) < 0) return false;
        if (date_.compareTo(endDate) > 0) return false;

        return true;
    }

    /**
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 开始日期 比 结束日期 早的年数
     */
    public static int getIntervalYears(Date beginDate, Date endDate){
        calendar.setTime(beginDate);
        int beginYear = calendar.get(Calendar.YEAR);
        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        return endYear - beginYear;
    }


    /**
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 开始日期和结束日期的间隔天数
     */
    public static long getIntervalDays(Date beginDate, Date endDate) {
        long days = 0L;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (beginDate != null) {
                String begin = sdf.format(beginDate);
                beginDate = sdf.parse(begin);
            }
            if (endDate != null) {
                String end = sdf.format(endDate);
                endDate = sdf.parse(end);
            }
            days = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 开始日期和结束日期的间隔毫秒数
     */
    public static long getIntervalMsecs(Date beginDate, Date endDate) {
        return endDate.getTime() - beginDate.getTime();
    }

    /**
     * @param date 指定日期
     * @return 指定日期的最小时间值
     */
    public static Date getMinTimeInDay(Date date){
        return parse(format(date, PATTERN_HYPHEN_YMD), PATTERN_HYPHEN_YMD);
    }

    /**
     * @param date 指定日期
     * @return 指定日期的最大时间值
     */
    public static Date getMaxTimeInDay(Date date){
        if(date == null){
            return null;
        }
        Date date_ = date;
        calendar.setTime(date_);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date_ = getMinTimeInDay(calendar.getTime());
        calendar.setTime(date_);
        calendar.add(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    /**
     * @param date 指定日期
     * @return 某月的第一天
     */
    public static Date getFirstDayByMonth(Date date){
        if(date == null){
            return null;
        }
        Date date_ = date;
        date_ = parse(format(date_, PATTERN_HYPHEN_YMD),PATTERN_HYPHEN_YMD );
        calendar.setTime(date_);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date 指定日期
     * @return 某月的最后一天
     */
    public static Date getLastDayByMonth(Date date) {
        if (date == null) {
            return null;
        }
        Date date_ = date;
        date_ = parse(format(date_, PATTERN_HYPHEN_YMD),PATTERN_HYPHEN_YMD );
        calendar.setTime(date_);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    private static Date removeNull(Date date) {
        Date date_ = date;
        if (date_ == null) {
            date_ = new Date();
        }
        return date_;
    }

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
//        System.out.println(format(date));
//        System.out.println(format(date,"yyMMddHHmmss"));
//        System.out.println(formatNow("yyMMdd"));

        System.out.println(format(addDaysThatDate(date, 2)));
//        System.out.println(format(addDaysThatDate(date,-1),PATTERN_HYPHEN_YMD) + "===" + format(addDaysThatDate(date,-1),PATTERN_HYPHEN_YMD));
//        for (int i = -500; i < 0; i++) {
//            System.out.println(format(addDaysThatDate(date,i),PATTERN_HYPHEN_YMD) + "===" + format(addDaysThatDate(date,i),PATTERN_HYPHEN_YMD));
//        }

//        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_HYPHEN_YMD_HMS);
//        String thatDateStr = "2019-01-01 13:01:00";
//        String beginDateStr = "2019-01-01 13:00:00";
//        String endDateStr = "2019-01-01 14:00:00";
//        Date thatDate = sdf.parse(thatDateStr);
//        Date beginDate = sdf.parse(beginDateStr);
//        Date endDate = sdf.parse(endDateStr);
//        System.out.println(isInDateRange(thatDate, null, endDate));
//        System.out.println(isInDateRange(thatDate, beginDate, null));
//        System.out.println(isInDateRange(thatDate, beginDate, endDate));
//        System.out.println(format(getThatDateStart(new Date())));

//        System.out.println(format(getMinTimeInDay(new Date())));
//        System.out.println(format(getMaxTimeInDay(new Date())));
//        System.out.println(format(getFirstDayByMonth(new Date())));
//
//        Date parse = parse("2012-02-03 11:12:02", PATTERN_HYPHEN_YMD_HMS);
//        System.out.println(format(getLastDayByMonth(parse)));
//        System.out.println(format(getLastDayByMonth(new Date())));
//        System.out.println(new Date().toString());

//        System.out.println("-----------------");
//        System.out.println(format(date, PATTERN_HYPHEN_YMD_HMS));
//        System.out.println(format(date, PATTERN_HYPHEN_YMD));
//        System.out.println(format(date, PATTERN_SLASH_YMD_HMS));
//        System.out.println(format(date, PATTERN_SLASH_YMD));
//        System.out.println(format(date, PATTERN_NUM_YMD_HMS));
//        System.out.println(format(date, PATTERN_NUM_YMD));



        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_HYPHEN_YMD);
        String thatDateStr =    "2019-01-01";
        String beginDateStr =   "2010-01-01";
        String endDateStr =     "2019-01-01";
        Date thatDate = sdf.parse(thatDateStr);
        Date beginDate = sdf.parse(beginDateStr);
        Date endDate = sdf.parse(endDateStr);
        System.out.println(getIntervalYears(beginDate, endDate));

    }

}
