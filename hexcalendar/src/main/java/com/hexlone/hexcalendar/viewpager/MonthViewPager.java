package com.hexlone.hexcalendar.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.view.BaseCalendarView;
import com.hexlone.hexcalendar.view.MonthView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public  class MonthViewPager extends BaseViewPager {

    protected LocalDate startDate;//开始date
    protected LocalDate endDate;//结束date
    protected int mPageSize;//pager数量
    protected int mCurrPager;//当前pager
    protected LocalDate nowDate;//日历初始化date，即今天
    protected LocalDate mSelectDate;//当前页面选中的date

    private MonthAdapter monthAdapter;

    public MonthViewPager(@NonNull Context context) {
        super(context);
        initDateAndAdapter();


        //Log.e("uuuu",getResources().getDimension(R.dimen.def_week_column_name_height)+"");
    }

    public MonthViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDateAndAdapter();
    }

    public void initDateAndAdapter() {
        nowDate = new LocalDate();
        startDate = new LocalDate("1901-01-01");
        endDate = new LocalDate("2099-12-31");
        if (nowDate.isBefore(startDate) || nowDate.isAfter(endDate)) {
            nowDate = new LocalDate("2011-12-19");
        }
        Log.d("nowDate",nowDate.getYear()+" - "+nowDate.getMonthOfYear()+" - "+nowDate.getDayOfMonth());
        mPageSize = DateUtils.getMonthsBetweenTwoDay(startDate, endDate) + 1;
        mCurrPager = DateUtils.getMonthsBetweenTwoDay(startDate, nowDate);
        setOffscreenPageLimit(2);
        //setPageTransformer(false,new ZoomPageTransformer(this));

        monthAdapter = new MonthAdapter(mPageSize,mCurrPager,nowDate);
        setAdapter(monthAdapter);
        setCurrentItem(mCurrPager);

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean isSure = false;
            int lastPosition=-1;
            float lastOff=-2;
            boolean isSlowToRight = false;
            boolean isFirstWork = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(positionOffset==0){

                    Log.e("wwwwww",position+"  "+getCurrentItem());
                    getMonthAdapter().cleanSelectedDay(position);
                    if(null!=pagerChangeListener&&!isFirstWork){
                        pagerChangeListener.pagerChange(position);
                    }
                    isFirstWork=false;
                }

                if(isSure&&positionOffset==0){
                    if(isSlowToRight){
                        lastPosition=lastPosition+1;
                    }else {
                        lastPosition=lastPosition-1;
                    }
                    isSlowToRight=false;
                    isSure=false;
                    lastOff=-2;
                    return;
                }
                if(lastOff==-2){
                    lastOff=positionOffset;
                }else {
                    if(positionOffset-lastOff>0){
                        //Log.e("wwww","向左");
                        isSlowToRight=false;
                    }else if(positionOffset-lastOff<0){
                        //Log.e("wwww","向右");
                        isSlowToRight=true;
                    }
                    lastOff=positionOffset;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Log.e("onPageSelected",position+" ");
                isSure = true;
                lastPosition=position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //Log.e("onPageScrollStateCha",state+" ");

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //Log.e("size ViewPager  ",getHeight()+" ");
    }

    public class MonthAdapter  extends PagerAdapter {
        protected int mCount;//总页数
        protected int mCurrentPager;//当前位置
        protected SparseArray<MonthView> mMonthViewList;
        protected List<Integer> listPosition ;
        protected LocalDate mDate;


        public SparseArray<BaseCalendarView> mBaseCalendarViewList;

        private boolean isHaveSelectedDay = false;
        private DateInfo dateInfo_selectedDay = null;

        public boolean isHaveSelectedDay() {
            return isHaveSelectedDay;
        }

        public DateInfo getDateInfo_selectedDay() {
            return dateInfo_selectedDay;
        }

        public MonthAdapter(int count, int currentPager, LocalDate date) {
            this.mDate = date;
            this.mCurrentPager = currentPager;
            this.mCount = count;
            mMonthViewList = new SparseArray<>();
            listPosition = new ArrayList<>();

            mBaseCalendarViewList = new SparseArray<>();

        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            BaseCalendarView baseCalendarView = mBaseCalendarViewList.get(position);
            if(baseCalendarView == null){
                baseCalendarView = new BaseCalendarView(mContext);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                baseCalendarView.setLayoutParams(layoutParams);
                baseCalendarView.setDate(DateUtils.getCurrentMonthInfo(mDate.
                        plusMonths(position-mCurrentPager)));
                if(position==mCurrentPager){
                    baseCalendarView.setNowDate(mDate);
                }
                mBaseCalendarViewList.put(position,baseCalendarView);
                listPosition.add(position);
            }
            container.addView(baseCalendarView);
            return baseCalendarView;
            /**
            MonthView monthView = (MonthView) mMonthViewList.get(position);
            //Log.d("instantiateItem",position+"   "+mMonthViewList.size());
            if (monthView == null) {
                monthView = new MonthView(mContext);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                monthView.setLayoutParams(layoutParams);
                monthView.setDate(DateUtils.getCurrentMonthInfo(mDate.plusMonths(position-mCurrentPager)));
                if(position==mCurrentPager){
                    monthView.setNowDate(mDate);
                }
                mMonthViewList.put(position, monthView);
                listPosition.add(position);
            }
            monthView.setSelectedDayListener(new MonthView.SelectedDayListener() {
                @Override
                public void selectedDay(DateInfo dateInfo, int x, int y) {
                    isHaveSelectedDay=true;
                    dateInfo_selectedDay = dateInfo;
                    if(null!=selectedDayListener){
                        selectedDayListener.selectedDay(dateInfo);
                    }
                    /**
                    if(isHaveSelectedDay){
                        int selectMonth=DateUtils.getMonthsBetweenTwoDay(mDate, new LocalDate(dateInfo.getSolarYear(),
                                dateInfo.getSolarMonth(),dateInfo.getSolarDay()));
                        selectMonth = selectMonth+mCurrentPager;
                        for(int i=0;i<listPosition.size();i++){
                            if(listPosition.get(i)==selectMonth) continue;
                            MonthView monthView1= mMonthViewList.get(listPosition.get(i));
                            //monthView1.cleanSelectedDay();
                        }
                    }

                    isHaveSelectedDay=true;
                    dateInfo_selectedDay = dateInfo;


                }


            });

            container.addView(monthView);
            return monthView;
             **/

        }

        public void cleanSelectedDay(int position){
            /**
            for(int i=0;i<listPosition.size();i++){
                MonthView monthView1= mMonthViewList.get(listPosition.get(i));
                monthView1.cleanSelectedDay();
            }
             **/
            //MonthView monthView = mMonthViewList.get(position);
            //monthView.cleanSelectedDay();
            if(isHaveSelectedDay){
                int selectMonth=DateUtils.getMonthsBetweenTwoDay(mDate, new LocalDate(dateInfo_selectedDay.getSolarYear(),
                        dateInfo_selectedDay.getSolarMonth(),dateInfo_selectedDay.getSolarDay()));
                selectMonth = selectMonth+mCurrentPager;
                MonthView monthView1= mMonthViewList.get(selectMonth);
                monthView1.cleanSelectedDay();
            }
            isHaveSelectedDay=false;
            dateInfo_selectedDay=null;


        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        public SparseArray<MonthView> getmMonthViewList() {
            return mMonthViewList;
        }

    }

    public MonthAdapter getMonthAdapter() {
        return monthAdapter;
    }
}
