package com.hexlone.hexcalendar.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.view.MonthView;
import com.hexlone.hexcalendar.view.WeekView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeekViewPager extends BaseViewPager {

    protected LocalDate startDate;//开始date
    protected LocalDate endDate;//结束date
    protected int mPageSize;//pager数量
    protected int mCurrPager;//当前pager
    protected LocalDate nowDate;//日历初始化date，即今天
    protected LocalDate mSelectDate;//当前页面选中的date

    private WeekAdapter weekAdapter;

    public WeekViewPager(@NonNull Context context) {
        super(context);

        initDateAndAdapter();

        //Log.e("uuuu",getResources().getDimension(R.dimen.def_week_column_name_height)+"");
    }

    public WeekViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        mPageSize = DateUtils.getWeeksBetweenTwoDay(startDate, endDate) + 1;
        mCurrPager = DateUtils.getWeeksBetweenTwoDay(startDate, nowDate);
        Log.e("page",mPageSize+" "+mCurrPager);
        setOffscreenPageLimit(5);
        //setPageTransformer(false,new ZoomPageTransformer(this));

        weekAdapter = new WeekViewPager.WeekAdapter(mPageSize,mCurrPager,nowDate);
        setAdapter(weekAdapter);
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
                    getWeekAdapter().cleanSelectedDay(position);
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

                isSure = true;
                lastPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public class WeekAdapter  extends PagerAdapter {
        protected int mCount;//总页数
        protected int mCurrentPager;//当前位置
        protected SparseArray<WeekView> mWeekViewList;
        protected List<Integer> listPosition ;
        protected LocalDate mDate;

        private boolean isHaveSelectedDay = false;
        private DateInfo dateInfo_selectedDay = null;


        public WeekAdapter(int count, int currentPager, LocalDate date) {
            this.mDate = date;
            this.mCurrentPager = currentPager;
            this.mCount = count;
            mWeekViewList = new SparseArray<>();
            listPosition = new ArrayList<>();

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


            WeekView weekView = (WeekView) mWeekViewList.get(position);
            //Log.d("instantiateItem",position+"   "+mMonthViewList.size());
            if (weekView == null) {
                weekView = new WeekView(mContext);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                weekView.setLayoutParams(layoutParams);
                weekView.setDate(DateUtils.getCurrentWeekInfo(mDate.plusDays((position-mCurrentPager)*7)));
                if(position==mCurrentPager){
                    weekView.setNowDate(mDate);
                }
                mWeekViewList.put(position, weekView);
                listPosition.add(position);
            }
            weekView.setSelectedDayListener(new WeekView.SelectedDayListener() {
                @Override
                public void selectedDay(DateInfo dateInfo, int x, int y) {

                    if(null!=selectedDayListener){
                        selectedDayListener.selectedDay(dateInfo);
                    }
                    /**
                    if(isHaveSelectedDay){
                        int selectWeek=DateUtils.getWeeksBetweenTwoDay(mDate, new LocalDate(dateInfo.getSolarYear(),
                                dateInfo.getSolarMonth(),dateInfo.getSolarDay()));
                        selectWeek = selectWeek+mCurrentPager;
                        for(int i=0;i<listPosition.size();i++){
                            if(listPosition.get(i)==selectWeek) continue;
                            WeekView WeekView1= mWeekViewList.get(listPosition.get(i));
                            //WeekView1.cleanSelectedDay();
                        }
                    }
                     **/
                    isHaveSelectedDay=true;
                    dateInfo_selectedDay = dateInfo;



                }


            });
            container.addView(weekView);
            return weekView;

        }
        public void cleanSelectedDay(int position){
            if(isHaveSelectedDay){
                int selectMonth=DateUtils.getWeeksBetweenTwoDay(mDate, new LocalDate(dateInfo_selectedDay.getSolarYear(),
                        dateInfo_selectedDay.getSolarMonth(),dateInfo_selectedDay.getSolarDay()));
                selectMonth = selectMonth+mCurrentPager;
                WeekView monthView1= mWeekViewList.get(selectMonth);
                monthView1.cleanSelectedDay();
            }else {

            }

            isHaveSelectedDay=false;
            dateInfo_selectedDay=null;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public SparseArray<WeekView> getmWeekViewList() {
            return mWeekViewList;
        }

    }

    public WeekAdapter getWeekAdapter() {
        return weekAdapter;
    }
}

