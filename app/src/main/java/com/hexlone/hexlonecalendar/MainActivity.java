package com.hexlone.hexlonecalendar;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hexlone.hexcalendar.HexCalendarLayout;
import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.utils.DpToPx;
import com.hexlone.hexcalendar.view.MonthView;
import com.hexlone.hexcalendar.viewpager.BaseViewPager;
import com.hexlone.hexcalendar.viewpager.MonthViewPager;
import com.hexlone.hexcalendar.viewpager.WeekViewPager;
import com.hexlone.hexlonecalendar.view.DateDisplayView;

import org.joda.time.LocalDate;


public class MainActivity extends AppCompatActivity {

    private MonthViewPager monthViewPager ;
    private WeekViewPager weekViewPager;
    private TextView textView;
    private HexCalendarLayout hexCalendarLayout;

    private DateDisplayView dateDisplayView_month,dateDisplayView_year;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateDisplayView_month = findViewById(R.id.dateDisplayView_month);
        dateDisplayView_year = findViewById(R.id.dateDisplayView_year);
        dateDisplayView_year.setTextSize(DpToPx.dpToPx(getApplicationContext(),15));


        //textView = findViewById(R.id.textView);
        hexCalendarLayout = findViewById(R.id.hexCalendarLayout);
        hexCalendarLayout.setTitleDayChange(new HexCalendarLayout.TitleDayChange() {
            @Override
            public void dayChange(DateInfo dateInfo) {
                //textView.setText(dateInfo.getSolarYear()+"年"+dateInfo.getSolarMonth()+"月"+
                //        dateInfo.getSolarDay()+"日");
                dateDisplayView_month.changeText(dateInfo.getSolarMonth());
                dateDisplayView_year.changeText(dateInfo.getSolarYear());
            }
        });




        /**
        monthViewPager = findViewById(R.id.monthViewPager);
        weekViewPager = findViewById(R.id.weekViewPager);

        int aa= DateUtils.getMonthsBetweenTwoDay(new LocalDate(2019,3,1),
                new LocalDate(2018,2,1));


        monthViewPager.setSelectedDayListener(new BaseViewPager.SelectedDayListener() {
            @Override
            public void selectedDay(DateInfo dateInfo) {
                Log.e("selsectdate",dateInfo.getSolarYear()+" "+dateInfo.getSolarMonth()
                        +" "+dateInfo.getSolarDay());

                isLinkage_MonthControlWeek=true;
                pagerLinkage_MonthControlWeek();
            }
        });

        monthViewPager.setPagerChangeListener(new BaseViewPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {
                Log.e("pager","dd");
                if(!isLinkage_WeekControlMonth){
                    isLinkage_MonthControlWeek=true;
                    pagerLinkage_MonthControlWeek();
                }else {
                    isLinkage_WeekControlMonth=false;
                }

            }
        });
        weekViewPager.setSelectedDayListener(new BaseViewPager.SelectedDayListener() {

            @Override
            public void selectedDay(DateInfo dateInfo) {
                Log.e("selsectdate",dateInfo.getSolarYear()+" "+dateInfo.getSolarMonth()
                        +" "+dateInfo.getSolarDay());
            }
        });

        weekViewPager.setPagerChangeListener(new BaseViewPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {
                if(isLinkage_MonthControlWeek){

                    if(monthViewPager.getMonthAdapter().isHaveSelectedDay()){
                        DateInfo dateInfo1 = monthViewPager.getMonthAdapter().getDateInfo_selectedDay();
                        LocalDate localDate =  new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                                dateInfo1.getSolarDay());

                        int y=localDate.getDayOfWeek();
                        if(y==7) y=0;

                        weekViewPager.getWeekAdapter().getmWeekViewList().get(weekViewPager.getCurrentItem())
                                .refreshSelectedDay(0,y);
                    }
                    isLinkage_MonthControlWeek=false;
                }else {

                    isLinkage_WeekControlMonth=true;
                    pagerLinkage_WeekControlMonth();
                }



            }
        });
         **/
    }


    private Boolean isLinkage_MonthControlWeek=false;
    private Boolean isLinkage_WeekControlMonth=false;

    public void pagerLinkage_MonthControlWeek(){
        //DateInfo dateInfo = monthViewPager.getMonthAdapter().getmMonthViewList().get(monthViewPager.getCurrentItem()).getList().get(15);
        if(monthViewPager.getMonthAdapter().isHaveSelectedDay()){
            DateInfo dateInfo1 = monthViewPager.getMonthAdapter().getDateInfo_selectedDay();
            LocalDate selectDate = new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                    dateInfo1.getSolarDay());
            int days = selectDate.getDayOfWeek();
            if(days==7){
                days=0;
            }
            selectDate=selectDate.plusDays(-days);
            //Log.e("selectDate",selectDate.getYear()+" "+selectDate.getMonthOfYear()+" "+selectDate.getDayOfMonth());
            DateInfo dateInfo2  =weekViewPager.getWeekAdapter().getmWeekViewList().get(weekViewPager.getCurrentItem()).getList().get(0);

            //Log.e("dateInfo2",dateInfo2.getSolarYear()+" "+dateInfo2.getSolarMonth()+" "+
            //        dateInfo2.getSolarDay());
            int weeks=DateUtils.getWeeksBetweenTwoDay(selectDate,new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                    dateInfo2.getSolarDay()));

            //Log.e("weeks",weeks+"");
            weeks = (-1)*weeks+weekViewPager.getCurrentItem();
            weekViewPager.setCurrentItem(weeks,false);


        }else {
            DateInfo dateInfo1 = monthViewPager.getMonthAdapter().getmMonthViewList()
                    .get(monthViewPager.getCurrentItem()).getList().get(0);

            LocalDate selectDate = new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                    dateInfo1.getSolarDay());
            int days = selectDate.getDayOfWeek();
            if(days==7){
                days=0;
            }
            selectDate=selectDate.plusDays(-days);

            //Log.e("selectDate",selectDate.getYear()+" "+selectDate.getMonthOfYear()+" "+selectDate.getDayOfMonth());
            DateInfo dateInfo2  =weekViewPager.getWeekAdapter().getmWeekViewList().get(weekViewPager.getCurrentItem()).getList().get(0);
            //Log.e("dateInfo2",dateInfo2.getSolarYear()+" "+dateInfo2.getSolarMonth()+" "+
            //        dateInfo2.getSolarDay());
            int weeks=DateUtils.getWeeksBetweenTwoDay(selectDate,new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                    dateInfo2.getSolarDay()));

            //Log.e("weeks",weeks+"");
            weeks = (-1)*weeks+weekViewPager.getCurrentItem();
            weekViewPager.setCurrentItem(weeks,false);
        }
    }
    public void pagerLinkage_WeekControlMonth(){



        DateInfo dateInfo1 = monthViewPager.getMonthAdapter().getmMonthViewList()
                .get(monthViewPager.getCurrentItem()).getList().get(0);

        LocalDate selectDate = new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                dateInfo1.getSolarDay());

            //Log.e("selectDate",selectDate.getYear()+" "+selectDate.getMonthOfYear()+" "+selectDate.getDayOfMonth());
        DateInfo dateInfo2  =weekViewPager.getWeekAdapter().getmWeekViewList().get(weekViewPager.getCurrentItem()).getList().get(0);
            //Log.e("dateInfo2",dateInfo2.getSolarYear()+" "+dateInfo2.getSolarMonth()+" "+
            //        dateInfo2.getSolarDay());

        int days=DateUtils.getDaysBetweenTwoDay(selectDate,new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                    dateInfo2.getSolarDay()));

        Log.e("days",days+"");
        if(days<0){
            monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()-1,false);
        }else if(days>0){

            //MonthView monthView=(MonthView)monthViewPager.getChildAt(monthViewPager.getCurrentItem());
            int a=monthViewPager.getMonthAdapter().getmMonthViewList().get(monthViewPager.getCurrentItem())
                    .getWeek_num();
            if(days>=a*7){
                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1,false);
            }

        }
        //weeks = (-1)*weeks+weekViewPager.getCurrentItem();
        //weekViewPager.setCurrentItem(weeks,false);

    }
}
