package com.hexlone.hexcalendar.utils;



import android.util.Log;

import com.hexlone.hexcalendar.entity.DateInfo;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    /**
     * 获取一个月里面的所有天的信息，包括当前页面里上个月和下个月的部分天
     * @param date
     * @return
     */

    public static List<DateInfo> getCurrentMonthInfo(LocalDate date){
        List<DateInfo> list = new ArrayList<>();
        list.clear();
        //上个月
        LocalDate lastMonth = date.plusMonths(-1);
        //下个月;
        LocalDate nextMonth = date.plusMonths(1);
        //当月天数
        int num_current_day = date.dayOfMonth().getMaximumValue();
        //上月天数
        int num_last_day = lastMonth.dayOfMonth().getMaximumValue();
        //当月第一天周几
        int current_firstDayOfWeek = new LocalDate(date.getYear(),
                date.getMonthOfYear(), 1).getDayOfWeek();
        //当月最后一天周几
        int current_endDayOfWeek = new LocalDate(date.getYear(),
                date.getMonthOfYear(),num_current_day).getDayOfWeek();

        //上月
        int year = lastMonth.getYear();
        int mon = lastMonth.getMonthOfYear();
        if (current_firstDayOfWeek != 7) {
            for (int i = 0; i < current_firstDayOfWeek; i++) {
                DateInfo dateInfo = XLeoneLunar.getDateInfo(year,mon,
                        num_last_day - (current_firstDayOfWeek - i - 1));
                dateInfo.setTypeOfMonth(-1);
                list.add(dateInfo);
            }
        }
        //当月
        year = date.getYear();
        mon = date.getMonthOfYear();
        for (int i = 0; i < num_current_day; i++) {
            DateInfo dateInfo = XLeoneLunar.getDateInfo(year,mon, i + 1);
            dateInfo.setTypeOfMonth(0);
            list.add(dateInfo);
        }
        //下月
        if (current_endDayOfWeek == 7) {
            current_endDayOfWeek = 0;
        }
        year = nextMonth.getYear();
        mon = nextMonth.getMonthOfYear();
        for (int i = 0; i < 6 - current_endDayOfWeek; i++) {
            DateInfo dateInfo = XLeoneLunar.getDateInfo(year,mon,i + 1);
            dateInfo.setTypeOfMonth(1);
            list.add(dateInfo);
        }
        return list;
    }

    public static List<DateInfo> getCurrentWeekInfo(LocalDate date){
        List<DateInfo> list = new ArrayList<>();
        list.clear();

        int dayOfWeek = date.getDayOfWeek();


        if(dayOfWeek !=7){
            date = date.plusDays((-1)*dayOfWeek);
        }

        for(int i=0;i<7;i++){

            DateInfo dateInfo = XLeoneLunar.getDateInfo(date.getYear(),date.getMonthOfYear(),
                    date.getDayOfMonth());
            dateInfo.setTypeOfMonth(0);
            list.add(dateInfo);
            date = date.plusDays(1);
        }
        return list;
    }

    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getMonthsBetweenTwoDay(LocalDate date1, LocalDate date2) {
        date1 = date1.withDayOfMonth(1);
        date2 = date2.withDayOfMonth(1);
        //Log.e("eeeeeeeee",date2.getYear()+" "+date2.getMonthOfYear()+" "+date2.getDayOfMonth());
        return Months.monthsBetween(date1, date2).getMonths();
    }

    /**
     * 获得两个日期距离几个周
     *
     * @return
     */
    public static int getWeeksBetweenTwoDay(LocalDate date1, LocalDate date2) {
        if (date1.dayOfWeek().get() != 7) {
            date1 = date1.minusWeeks(1).withDayOfWeek(7);
        }
        if (date2.dayOfWeek().get() != 7) {
            date2 = date2.minusWeeks(1).withDayOfWeek(7);
        }
        //Log.e("eeeeeeeee1",date1.getYear()+" "+date1.getMonthOfYear()+" "+date1.getDayOfMonth());
        //Log.e("eeeeeeeee2",date2.getYear()+" "+date2.getMonthOfYear()+" "+date2.getDayOfMonth());
        return Weeks.weeksBetween(date1, date2).getWeeks();
    }

    /**
     * 获得两个日期距离几个周
     *
     * @return
     */
    public static int getDaysBetweenTwoDay(LocalDate date1, LocalDate date2) {
        //Log.e("eeeeeeeee1",date1.getYear()+" "+date1.getMonthOfYear()+" "+date1.getDayOfMonth());
        //Log.e("eeeeeeeee2",date2.getYear()+" "+date2.getMonthOfYear()+" "+date2.getDayOfMonth());
        return Days.daysBetween(date1, date2).getDays();
    }
}
