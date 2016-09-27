package com.games.mmo.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtil
{

    /**
     * 获取系统距1970年1月1日总毫秒
     * 
     * @return
     */
    public static long getSysCurTimeMillis()
    {
        return getCalendar().getTimeInMillis();
    }

    /**
     * 获取系统距1970年1月1日总秒
     * 
     * @return
     */
    public static long getSysCurSeconds()
    {
        return getCalendar().getTimeInMillis() / 1000;
    }

    /**
     * 获取系统当前时间
     * 
     * @return
     */
    public static Timestamp getSysteCurTime()
    {
        Timestamp ts = new Timestamp(getCalendar().getTimeInMillis());
        return ts;
    }

    /**
     * 获取系统的月份和年份，日期置为1，其它默认为0
     * 
     * @return 2013-05-01 00:00:00.0
     */
    public static Timestamp getSysMonth()
    {
        java.util.Calendar now = getCalendar();
        now.set(Calendar.DAY_OF_MONTH, 1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.format(now.getTime());
        return new Timestamp(now.getTimeInMillis());
    }

    /**
     * 获取指定日期距1970年1月1日总秒
     * 
     * @param date
     * @return
     */
    public static long getDateToSeconds(Date date)
    {
        return getCalendar(date).getTimeInMillis() / 1000;
    }

    /**
     * 获取当前时间的秒
     * 
     * @return
     */
    public static int getSysTimeSeconds()
    {
        Calendar cal = getCalendar();
        return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE)
                * 60 + cal.get(Calendar.SECOND);
    }

    /**
     * 获取指定日期距1970年1月1日总毫秒
     * 
     * @param date
     * @return
     */
    public static long getDateToMillis(Date date)
    {
        return getCalendar(date).getTimeInMillis();
    }

    /**
     * 获取当前小时
     * 
     * @return
     */
    public static int getCurrentHour()
    {
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获 取当前分钟
     * 
     * @return
     */
    public static int getCurrentMinute()
    {
        return getCalendar().get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒
     * 
     * @return
     */
    public static int getCurrentSecond()
    {
        return getCalendar().get(Calendar.SECOND);
    }

    /**
     * 获取当前天
     */
    public static int getCurrentDay()
    {
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前Date
     * 
     * @return
     */
    public static Date getCurrentDate()
    {
        Calendar cal = getCalendar();
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 指定的毫秒long值转成Timestamp类型
     * 
     * @param value
     * @return
     */
    public static java.sql.Timestamp getMillisToDate(long value)
    {
        return new java.sql.Timestamp(value);
    }

    /**
     * 当前系统时间增加值
     * 
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSystemCurTime(int type, int value)
    {
        Calendar cal = getCalendar();
        switch (type)
        {
        case Calendar.DATE:// 增加天数
            cal.add(Calendar.DATE, value);
            break;
        case Calendar.HOUR:// 增加小时
            cal.add(Calendar.HOUR, value);
            break;
        case Calendar.MINUTE:// 增加分钟
            cal.add(Calendar.MINUTE, value);
            break;
        case Calendar.SECOND:// 增加秒
            cal.add(Calendar.SECOND, value);
            break;
        case Calendar.MILLISECOND:// 增加毫秒
            cal.add(Calendar.MILLISECOND, value);
            break;
        default:
            break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 获取下一天的日期
     * 
     * @return
     */
    public static Date getNextDate()
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @return
     */
    public static String getDateFormat(java.util.Date date)
    {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(date);
        return ctime;
    }

    /**
     * 自定义格式化日期
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static String getDateFormat(java.util.Date date, String dateFormat)
    {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(dateFormat);
        String ctime = formatter.format(date);
        return ctime;
    }

    /**
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static String getDateFormat(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(new Long(timeStamp));
        Date date = null;
		try {
			date = formatter.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return formatter.format(date);
    }
    /**
     * 自定义格式化日期, 支持设置语言区
     * 
     * @param date
     * @param dateFormat
     * @param locale
     * @return
     */
    public static String getDateFormat(java.util.Date date, String dateFormat,
            Locale locale)
    {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(dateFormat, locale);
        String ctime = formatter.format(date);
        return ctime;
    }

    /**
     * 获取默认日期2000-01-01
     * 
     * @return 返回默认起始时间
     */
    public static java.sql.Timestamp getDefaultDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2000-01-01 00:00:00");

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 获取默认目上限日期2999-01-01
     * 
     * @return 返回默认上限时间
     */
    public static java.sql.Timestamp getDefaultMaxDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2999-01-01 00:00:00");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 比较日期是否同一天
     * 
     * @param date
     * @return
     */
    public static boolean dateCompare(Date date)
    {
        if (date == null)
            return false;
        java.util.Calendar now = getCalendar();
        java.util.Calendar other = getCalendar(date);
        return dateCompare(now, other) == 0 ? true : false;
    }

    /**
     * 比较日期是否同一天
     * 
     * @param date
     * @return
     */
    public static boolean dateCompare(long date)
    {
        java.util.Calendar now = getCalendar();
        java.util.Calendar other = getCalendar(getMillisToDate(date));
        return dateCompare(now, other) == 0 ? true : false;
    }

    public static boolean dataCompare5(Date date)
    {
        if (date == null)
            return false;
        java.util.Calendar now = getCalendar();
        now.add(Calendar.HOUR_OF_DAY, -5);
        java.util.Calendar other = getCalendar(date);
        other.add(Calendar.HOUR_OF_DAY, -5);
        if (dateCompare(now, other) == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 比较两个时间是否相等
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean dataCompare(Date date1, Date date2)
    {
        if (date1 == null || date2 == null)
            return false;
        java.util.Calendar c1 = getCalendar(date1);
        java.util.Calendar c2 = getCalendar(date2);
        return dateCompare(c1, c2) == 0 ? true : false;
    }

    /**
     * 返回两个日期相差天数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int dateCompare(java.util.Calendar startDate,
            java.util.Calendar endDate)
    {
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        int day = (int) (endDate.getTimeInMillis() / 1000 / 60 / 60 / 24 - startDate
                .getTimeInMillis() / 1000 / 60 / 60 / 24);
        return day;
    }

    /**
     * 返回两个日期相差天数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int dateCompare(Date startDate, Date endDate)
    {
        if (startDate == null || endDate == null)
        {
            return 0;
        }
        java.util.Calendar c1 = getCalendar(startDate);
        java.util.Calendar c2 = getCalendar(endDate);
        return dateCompare(c1, c2);
    }

    /**
     * 比较日期是否是同一个月份
     * 
     * @param date
     *            被比较的日期
     * @return
     */
    public static boolean monthCompare(Date date)
    {// 一年之内是否是同一个月
        if (date == null)
            return false;
        java.util.Calendar now = getCalendar();
        java.util.Calendar other = getCalendar(date);
        int nowMonth = now.get(Calendar.MONTH) + 1;
        int otherMonth = other.get(Calendar.MONTH) + 1;
        return (otherMonth - nowMonth) == 0 ? true : false;
    }

    /**
     * 获取该月的天数
     * 
     * @return
     */
    public static int monthDays()
    {// 返回当前月份的天数
        java.util.Calendar now = getCalendar();
        return now.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前是该月的第几天
     * 
     * @return
     */
    public static int monthDay()
    {
        java.util.Calendar now = getCalendar();
        return now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 重置防沉迷刷新时间
     * 
     * @param hour
     *            刷新时间点
     * @param refreshTime
     *            刷新时间引用
     */
    public static void setAASRefreshTime(int hour, Calendar refreshTime)
    {
        refreshTime.setTime(getSysteCurTime());
        refreshTime.set(Calendar.HOUR_OF_DAY, hour);
        refreshTime.set(Calendar.MINUTE, 0);
        refreshTime.set(Calendar.SECOND, 0);
    }

    /**
     * 计算两个时间的时间差
     * 
     * @param startTime
     * @param endTime
     * @return 时间差毫秒
     */
    public static long calcDistanceMillis(Date startTime, Date endTime)
    {
        long startSecond = getDateToMillis(startTime);
        long endSecond = getDateToMillis(endTime);
        return (endSecond - startSecond);
    }

    /**
     * 间隔时间以小时为单位
     * 
     * @param startDate
     * @param interval
     * @return
     */
    public static boolean isInterval(Date startDate, int interval)
    {

        return dataCompare5(startDate);
    }

    /**
     * 将时间转换成帧数，这里属40ms为一帧
     * 
     * @param secondTime
     *            时间
     * @return
     */
    public static int timeToFrame(int secondTime)
    {
        return (secondTime * 25) / 1000;
    }

    /**
     * 获取系统时间
     * 
     * @return
     */
    private static java.util.Calendar getCalendar()
    {
        java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
        nowCalendar.setTime(new java.util.Date());
        return nowCalendar;
    }

    /**
     * 获取指定的时间
     * 
     * @param date
     * @return
     */
    public static java.util.Calendar getCalendar(Date date)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 将Calendar时间转换成Timestamp
     * 
     * @param calendar
     * @return
     */
    public static Timestamp getCalendarToDate(java.util.Calendar calendar)
    {
        if (calendar != null)
            return new Timestamp(getCalendar().getTimeInMillis());
        return null;
    }

    /**
     * 给给定的时间添加上时间值value
     * 
     * @param date
     *            源时间
     * @param value
     *            添加的时间值
     * @return
     */
    public static Date addDate(Date date, long value)
    {
        long time = date.getTime() + value;
        return new Date(time);
    }

    /**
     * 把日期类型转换为字节数组
     * 
     * @param date
     * @return
     */
    public static byte[] dateToBytes(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        byte[] byteArray = new byte[7];
        calendar.setTime(date);
        short year = (short) calendar.get(Calendar.YEAR);
        byteArray[0] = (byte) ((year >>> 8) & 0xFF);
        byteArray[1] = (byte) (year & 0xFF);
        byteArray[2] = (byte) (calendar.get(Calendar.MONTH) + 1);
        byteArray[3] = (byte) calendar.get(Calendar.DATE);
        byteArray[4] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        byteArray[5] = (byte) calendar.get(Calendar.MINUTE);
        byteArray[6] = (byte) calendar.get(Calendar.SECOND);
        return byteArray;
    }

    /**
     * 获取下一个星期天的日期
     * 
     * @return
     */
    public static Date getNextSunday()
    {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date monday = currentDate.getTime();
        return monday;
    }

    /**
     * 获取下一个星期一的日期
     * 
     * @return
     */
    public static Date getNextMonday()
    {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date monday = currentDate.getTime();
        return monday;
    }

    private static int getMondayPlus()
    {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1)
        {
            return 0;
        }
        else
        {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取当日处于这周的哪一天 从星期一开始，1表示星期一，2表示星期二.....
     * 
     * @return
     */
    public static int getDayOfWeekIndex()
    {
        Calendar calendar = Calendar.getInstance();
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (index == 0)
        {
            index = 7;
        }
        return index;
    }

    /**
     * 与当前时间对比，判断是否超时
     * 
     * @param expDate
     * @return
     */
    public static boolean isTimeOut(Date expDate)
    {
        Calendar curentDate = Calendar.getInstance();
        Calendar expirtDate = Calendar.getInstance();
        expirtDate.setTime(expDate);

        long intervalMillis = expirtDate.getTimeInMillis()
                - curentDate.getTimeInMillis();
        return intervalMillis <= 0;
    }

    /**
     * 获取星期六的日期
     * 
     * @param nextWeek
     *            表示哪一周，0表示当周，1表示下一周，后面类似
     * @return
     */
    public static Date getSaturday(int nextWeek)
    {
        int mondayPlus = getMondayPlus();
        if (nextWeek > 0)
        {
            mondayPlus = mondayPlus + (nextWeek * 7);
        }
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 5);
        currentDate.set(Calendar.HOUR_OF_DAY, 5);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date saturday = currentDate.getTime();
        return saturday;
    }

    /**
     * 判断今天是不是星期六
     * 
     * @return
     */
    public static boolean isSaturday()
    {
        int dayIndex = getDayOfWeekIndex();
        if (6 == dayIndex)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是一周中的某一天
     * 
     * @param weekDay
     * @return
     */
    public static boolean isWeekDay(int weekDay)
    {
        return getDayOfWeekIndex() == weekDay;
    }

    /**
     * 判断是否是月份中的某一天
     * 
     * @param monthDay
     * @return
     */
    public static boolean isMonthDay(Date date, int monthDay)
    {
        Calendar compareDate = Calendar.getInstance();
        compareDate.setTime(date);
        return compareDate.get(Calendar.DATE) == monthDay;
    }

    /**
     * 解析时间
     * 
     * @param dateStr
     *            时间字符串 格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parseDate(String dateStr)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try
        {
            date = df.parse(dateStr);
            return date;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回两个日期相隔的（小时，分钟，秒）
     * 
     * @param startTime
     * @param endTime
     * @param type
     *            [day,hour,min,sec]
     * @return
     */
    public static int timeSpan(Date startTime, Date endTime, String type)
    {

        if (startTime == null || endTime == null)
            return 0;
        long span = endTime.getTime() - startTime.getTime();
        if (type.equalsIgnoreCase("day"))
            return (int) (span / (24 * 60 * 60 * 1000));
        else if (type.equalsIgnoreCase("hour"))
            return (int) (span / (60 * 60 * 1000));
        else if (type.equalsIgnoreCase("min"))
            return (int) (span / (60 * 1000));
        else if (type.equalsIgnoreCase("sec"))
            return (int) (span / 1000);
        else
            return (int) span;
    }

    /**
     * 特定时间增加值
     * 
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSpecialCurTime(Date date, int type,
            int value)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (type)
        {
        case Calendar.DATE:// 增加天数
            cal.add(Calendar.DATE, value);
            break;
        case Calendar.HOUR:// 增加小时
            cal.add(Calendar.HOUR, value);
            break;
        case Calendar.MINUTE:// 增加分钟
            cal.add(Calendar.MINUTE, value);
            break;
        case Calendar.SECOND:// 增加秒
            cal.add(Calendar.SECOND, value);
            break;
        case Calendar.MILLISECOND:// 增加毫秒
            cal.add(Calendar.MILLISECOND, value);
            break;
        default:
            System.err.println("当前类型不存在！");
            break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 比较两个日期是否在同一周内<br>
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isInSameWeek(Date date1, Date date2)
    {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        if (c1.get(Calendar.WEEK_OF_YEAR) != c2.get(Calendar.WEEK_OF_YEAR))
        {
            if (Math.abs(c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) > 1)
            {
                // 如果年之间大于1年, 则肯定不是同一周
                return false;
            }

            // 其他情况则判断 date2 是否在 date1 的周一到周日中
            int mondayOffset = c1.get(Calendar.DAY_OF_WEEK) - 1;
            if (mondayOffset == 0)
            {
                mondayOffset = 7;
            }

            // 计算date1所在周的星期一时间
            Calendar monday = Calendar.getInstance();
            monday.setTime(date1);
            monday.add(Calendar.DATE, -mondayOffset);
            monday.set(Calendar.HOUR_OF_DAY, 0);
            monday.set(Calendar.MINUTE, 0);
            monday.set(Calendar.SECOND, 0);

            // 计算date1所在周的星期日时间
            Calendar sunday = Calendar.getInstance();
            sunday.setTime(monday.getTime());
            sunday.add(Calendar.DATE, 7);
            sunday.set(Calendar.HOUR_OF_DAY, 23);
            sunday.set(Calendar.MINUTE, 59);
            sunday.set(Calendar.SECOND, 59);

            // System.out.printf("周一开始时间:[%s]\n",
            // getDateFormat(monday.getTime()));
            // System.out.printf("周日结束时间:[%s]\n",
            // getDateFormat(sunday.getTime()));

            return date2.getTime() >= monday.getTimeInMillis()
                    && date2.getTime() <= sunday.getTimeInMillis();
        }
        else
        {
            // 如果周数不同, 则可确定不是同一周
            return true;
        }

    }

    /**
     * 判断今天是否是指定的星期几
     * 
     * @param weekDay
     *            1为星期1....不写了
     * @return
     */
    public static boolean isWeekDay(Date date, int weekDay)
    {
        if (weekDay > 7 || weekDay < 1)
        {
            return false;
        }
        if (weekDay == 7)
        {
            weekDay = 1;
        }

        Calendar compareDate = Calendar.getInstance();
        compareDate.setTime(date);
        return compareDate.get(Calendar.DAY_OF_WEEK) == (weekDay + 1);
    }

    /**
     * 指定时间是否在时间段内
     * 
     * @param timePeriod
     * @param date
     * @return
     */
    public static int isInTimePeriod(String startTime, String endTime)
    {
        return isInTimePeriod(getCalendar(startTime), getCalendar(endTime));
    }

    /**
     * 根据传入的时间获取calendar,时间格式类似是：12:00
     * 
     * @param time
     * @return
     */
    public static Calendar getCalendar(String time)
    {
        // 活动开始时间
        String strs[] = time.split("\\:");
        int hour = Integer.valueOf(strs[0]);
        int minute = Integer.valueOf(strs[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        // PrintUtil.print(calendar.get(Calendar.HOUR_OF_DAY));
        return calendar;
    }

    private static int isInTimePeriod(Calendar startTime, Calendar endTime)
    {
        Calendar now = getCalendar();

        int nowMinute = now.get(Calendar.HOUR_OF_DAY) * 60
                + now.get(Calendar.MINUTE);
        int startMinute = startTime.get(Calendar.HOUR_OF_DAY) * 60
                + startTime.get(Calendar.MINUTE);
        int endMinute = endTime.get(Calendar.HOUR_OF_DAY) * 60
                + endTime.get(Calendar.MINUTE);

        if (nowMinute < startMinute)
            return -1;
        else if (nowMinute >= startMinute && nowMinute <= endMinute - 1)
            return nowMinute - startMinute;
        else
            return -1;
    }
}
