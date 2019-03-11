package com.hexlone.hexcalendar.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.hexlone.hexcalendar.HexCalendarLayout;
import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.utils.DpToPx;
import com.hexlone.hexcalendar.utils.XLeoneLunar;
import com.hexlone.hexcalendar.view.BaseCalendarView;
import com.hexlone.hexcalendar.view.MonthView;
import com.hexlone.hexcalendar.view.WeekView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class BaseCalendarPager extends ViewPager {

    protected Context mContext;


    protected LocalDate startDate;//开始date
    protected LocalDate endDate;//结束date
    protected int mPageSize;//pager数量
    protected int mCurrPager;//当前pager
    protected LocalDate nowDate;//日历初始化date，即今天
    protected LocalDate mSelectDate;//当前页面选中的date

    private BaseAdapter baseAdapter;



    protected SelectedDayListener selectedDayListener = null;

    protected PagerChangeListener pagerChangeListener = null;

    public BaseCalendarPager(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public BaseCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,null);
    }
    private void init(Context context,AttributeSet attributeSet){
        this.mContext = context;
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public static int MONTH_VIEW=1;
    public static int WEEK_VIEW=2;

    private int type_calendar=0;

    public int getType_calendar() {
        return type_calendar;
    }

    public void initDateAndAdapter(final int type_calendar) {
        this.type_calendar=type_calendar;
        nowDate = new LocalDate();
        startDate = new LocalDate("1901-01-01");
        endDate = new LocalDate("2099-12-31");
        if (nowDate.isBefore(startDate) || nowDate.isAfter(endDate)) {
            nowDate = new LocalDate("2011-12-19");
        }

        if(type_calendar== MONTH_VIEW){
            mPageSize = DateUtils.getMonthsBetweenTwoDay(startDate, endDate) + 1;
            mCurrPager = DateUtils.getMonthsBetweenTwoDay(startDate, nowDate);
        }else {
            mPageSize = DateUtils.getWeeksBetweenTwoDay(startDate, endDate) + 1;
            mCurrPager = DateUtils.getWeeksBetweenTwoDay(startDate, nowDate);
        }

        setOffscreenPageLimit(2);
        //setPageTransformer(false,new ZoomPageTransformer(this));

        baseAdapter = new BaseAdapter(mPageSize,mCurrPager,nowDate);
        setAdapter(baseAdapter);
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


                    if(position==lastPosition){
                        return;
                    }


                    if(!isFirstWork){
                        getBaseAdapter().cleanSelectedDay(lastPosition);
                    }

                    if(null!=pagerChangeListener&&!isFirstWork){
                        pagerChangeListener.pagerChange(position);
                    }
                    isFirstWork=false;
                    lastPosition = getCurrentItem();
                    if(null!=translationYListener&&type_calendar==MONTH_VIEW&&getVisibility()==VISIBLE){
                        //Log.e("wwwwww",position+"  "+lastPosition);
                        translationYListener.translationY(getBaseAdapter().getmBaseCalendarViewList().
                                get(position).getWeek_num()*DpToPx.dpToPx(mContext,50));
                    }
                    return;

                }

                if(type_calendar==WEEK_VIEW) return;
                Log.e("onPager",position+"  "+positionOffset);
                float height=0;
                int now =0;
                int next = 0;
                if (position < lastPosition) {
                    //右滑
                    if(Math.abs(position-lastPosition)>1){
                        lastPosition=position+1;
                    }
                    now = getBaseAdapter().getmBaseCalendarViewList().get(lastPosition).getWeek_num();
                    next = getBaseAdapter().getmBaseCalendarViewList().get(position).getWeek_num();
                    height = (now-(now-next)*(1-positionOffset))*weekPagerHeight;
                } else if(position>=lastPosition){
                    //左滑
                    if(position>lastPosition){
                        lastPosition=position+1;
                    }
                    now = getBaseAdapter().getmBaseCalendarViewList().get(lastPosition).getWeek_num();
                    next = getBaseAdapter().getmBaseCalendarViewList().get(position+1).getWeek_num();
                    height = (now-(now-next)*(positionOffset))*weekPagerHeight;
                }

                if(null!=translationYListener){
                    translationYListener.translationY(height);
                }



            }

            int selectedPager=-1;

            @Override
            public void onPageSelected(int position) {
                //Log.e("onPageSelected",position+" ");
                isSure = true;
                selectedPager=position;
                //lastPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //Log.e("onPageScrollStateCha",state+" ");

            }
        });
    }


    public int getmCurrPager() {
        return mCurrPager;
    }

    public class BaseAdapter extends PagerAdapter{
        protected int mCount;//总页数
        protected int mCurrentPager;//当前位置

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

        public BaseAdapter(int count, int currentPager, LocalDate date) {
            this.mDate = date;
            this.mCurrentPager = currentPager;
            this.mCount = count;
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

                if(type_calendar==MONTH_VIEW){
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, DpToPx.dpToPx(mContext,300));
                    baseCalendarView.setLayoutParams(layoutParams);
                    baseCalendarView.setDate(DateUtils.getCurrentMonthInfo(mDate.
                            plusMonths(position-mCurrentPager)));
                }else {
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, DpToPx.dpToPx(mContext,50));
                    baseCalendarView.setLayoutParams(layoutParams);
                    baseCalendarView.setDate(DateUtils.getCurrentWeekInfo(mDate.
                            plusDays((position-mCurrentPager)*7)));
                }


                mBaseCalendarViewList.put(position,baseCalendarView);
                listPosition.add(position);

                if(position==mCurrentPager){
                    int x=baseCalendarView.setNowDate(mDate);
                    setInvisibleFlagDay(XLeoneLunar.getDateInfo(mDate.getYear(),mDate.getMonthOfYear(),
                            mDate.getDayOfMonth()),x,mDate.getDayOfWeek());
                }
            }
            baseCalendarView.setSelectedDayListener(new BaseCalendarView.SelectedDayListener() {
                @Override
                public void selectedDay(DateInfo dateInfo, int x, int y) {
                    isHaveSelectedDay=true;
                    dateInfo_selectedDay = dateInfo;
                    if(null!=selectedDayListener){
                        selectedDayListener.selectedDay(dateInfo,x,y);
                    }
                }
            });


            container.addView(baseCalendarView);
            return baseCalendarView;
        }

        public void cleanSelectedDay(int position){
            int selectMonth=-1;

            if(type_calendar==MONTH_VIEW){
                selectMonth=DateUtils.getMonthsBetweenTwoDay(mDate,
                        getInvisibleFlagDay());
                selectMonth = selectMonth+mCurrentPager;
            }else {
                selectMonth=DateUtils.getWeeksBetweenTwoDay(mDate,
                        getInvisibleFlagDay());
                selectMonth = selectMonth+mCurrentPager;
            }


            mBaseCalendarViewList.get(selectMonth).cleanSelectedDay();
        }



        private DateInfo invisibleFlagDateInfo = null;

        public DateInfo getInvisibleFlagDateInfo() {
            return invisibleFlagDateInfo;
        }

        public void setInvisibleFlagDay(DateInfo dateInfo, int x, int y){

            invisibleFlagDateInfo = dateInfo;

            if(null!=titleDayChange){
                titleDayChange.dayChange(dateInfo);
            }
            LocalDate invisibleFlagDay=new LocalDate(dateInfo.getSolarYear(),
                    dateInfo.getSolarMonth(),dateInfo.getSolarDay());

            setInvisibleFlagDay1(invisibleFlagDay);
            //DateInfo dateInfo1 = mBaseCalendarViewList.get()
            //LocalDate localDate = new LocalDate()
            if(type_calendar==MONTH_VIEW){
                Log.e("Month_InvisibleFlag",invisibleFlagDay+"  wwdw");
                getmBaseCalendarViewList().get(getCurrentItem()).refreshSelectedDay(x,y);
            }else {
                Log.e("Week_InvisibleFlag",invisibleFlagDay+"  wwdw");
                getmBaseCalendarViewList().get(getCurrentItem()).refreshSelectedDay(x,y);
            }

        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public SparseArray<BaseCalendarView> getmBaseCalendarViewList() {
            return mBaseCalendarViewList;
        }
    }





    private LocalDate invisibleFlagDay = null;

    public int getInvisibleFlagDay_Y(int position){
        int a=0;
        DateInfo dateInfo = getBaseAdapter().getmBaseCalendarViewList().get(position).getList().get(0);
        LocalDate localDate = new LocalDate(dateInfo.getSolarYear(),
                dateInfo.getSolarMonth(),dateInfo.getSolarDay());

        //Log.e("wwww",getInvisibleFlagDay().toString());
        a=DateUtils.getDaysBetweenTwoDay(localDate,getInvisibleFlagDay());
        a=a/7;
        return a;
    }


    public void setInvisibleFlagDay1(LocalDate invisibleFlagDay) {
        this.invisibleFlagDay = invisibleFlagDay;
    }

    public LocalDate getInvisibleFlagDay() {
        if(null==invisibleFlagDay){
            invisibleFlagDay = new LocalDate();
        }
        return invisibleFlagDay;
    }

    public BaseAdapter getBaseAdapter() {
        return baseAdapter;
    }

    public void setSelectedDayListener(SelectedDayListener selectedDayListener) {
        this.selectedDayListener = selectedDayListener;
    }

    public interface SelectedDayListener{
        void selectedDay(DateInfo dateInfo,int x,int y);
    }

    public void setPagerChangeListener(PagerChangeListener pagerChangeListener) {
        this.pagerChangeListener = pagerChangeListener;
    }

    public interface PagerChangeListener{
        void pagerChange(int position);
    }


    private TitleDayChange titleDayChange = null;

    public void setTitleDayChange(TitleDayChange titleDayChange) {
        this.titleDayChange = titleDayChange;
    }

    public interface TitleDayChange{
        void dayChange(DateInfo dateInfo);
    }

    private float downX,downY;
    private float mLastX,mLastY;


    int weekPagerHeight ;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(getHeight()>300){
            weekPagerHeight = getHeight()/6;
        }else {
            weekPagerHeight = getHeight();
        }
        weekPagerHeight = DpToPx.dpToPx(mContext,50);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                mLastX = downX = x;
                mLastY = downY = y;
                //Log.e("pager_action_Down",downX+" ");
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;

                if(dx!=0&& y<getBaseAdapter().getmBaseCalendarViewList()
                        .get(getCurrentItem()).getWeek_num()*weekPagerHeight){
                    //Log.e("Pager_action_move",dx+" ");
                    mLastX = x;
                    return true;
                }

                 /*
                   如果向上滚动，且ViewPager已经收缩，不拦截事件
                 */


                break;
            case MotionEvent.ACTION_UP:

                /**
                Log.e("up",mLastX+"  "+x);
                if(mLastX==x&&mLastY==y){
                    return false;
                }
                 **/
        }
        return super.onInterceptTouchEvent(ev);
    }

    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = downX = x;

                mLastY = downY = y;

                break;
            /**
             case MotionEventCompat.ACTION_POINTER_DOWN: {
             final int indexx = MotionEventCompat.getActionIndex(event);
             mActivePointerId = MotionEventCompat.getPointerId(event, indexx);
             if (mActivePointerId == 0) {
             //核心代码：就是让下面的 dy = y- mLastY == 0，避免抖动
             mLastY = MotionEventCompat.getY(event, mActivePointerId);
             }
             break;
             }
             **/
            case MotionEvent.ACTION_MOVE:


                //Log.e("pager_onte_action_move",downX+" "+x);
                float dx = x - mLastX;
                float dy = y - mLastY;
                int position = getCurrentItem();
                int nextPosition = 0;
                if(dx==0){
                    return false;
                }
                if(dx>0){
                    if(position == 0){
                        nextPosition =0;
                    }else {
                        nextPosition = position-1;
                    }


                }else {
                    if(position == getChildCount()){
                        nextPosition = position;
                    }else {
                        nextPosition = position+1;
                    }
                }



                mLastX = y;
                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                float mYVelocity = velocityTracker.getYVelocity();
                //Log.e("azazazazaz",mYVelocity+" "+mMaximumVelocity);

                if (Math.abs(mYVelocity) >= 800) {
                    if (mYVelocity < 0) {
                        //shrink();
                    } else {
                        //expand();
                    }
                    return super.onTouchEvent(event);
                }
                if (event.getY() - downY > 0) {
                    //expand();
                } else {
                    //shrink();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private TranslationYListener translationYListener =null;

    public void setTranslationYListener(TranslationYListener translationYListener) {
        this.translationYListener = translationYListener;
    }

    public interface TranslationYListener{
        void translationY(float offSet);
    }

}
