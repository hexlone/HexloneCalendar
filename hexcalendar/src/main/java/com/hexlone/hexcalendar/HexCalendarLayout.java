package com.hexlone.hexcalendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.utils.DpToPx;
import com.hexlone.hexcalendar.utils.XLeoneLunar;
import com.hexlone.hexcalendar.view.BaseCalendarView;
import com.hexlone.hexcalendar.viewpager.BaseCalendarPager;
import com.hexlone.hexcalendar.viewpager.MonthViewPager;
import com.hexlone.hexcalendar.viewpager.WeekViewPager;

import org.joda.time.LocalDate;

import java.util.List;

public class HexCalendarLayout extends LinearLayout {

    private Context mContext;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    public HexCalendarLayout(Context context) {
        super(context);
        init(context,null);
    }

    public HexCalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public HexCalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    //private MonthViewPager monthViewPager;
    //private WeekViewPager weekViewPager;

    private BaseCalendarPager monthViewPager,weekViewPager;
    private HexCalendarView hexCalendarView;
    private NestedScrollView nestedScrollView;

    private Boolean isLinkage_MonthControlWeek=false;
    private Boolean isLinkage_WeekControlMonth=false;
    private void init(Context context,AttributeSet attributeSet){
        this.mContext=context;
        setMotionEventSplittingEnabled(false);
        setOrientation(LinearLayout.VERTICAL);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();


        View root = inflate(mContext,R.layout.layout_hexcalendarview,null);
        hexCalendarView = root.findViewById(R.id.hexCalendarView);
        monthViewPager = root.findViewById(R.id.monthViewPager);
        weekViewPager = root.findViewById(R.id.weekViewPager);
        nestedScrollView = root.findViewById(R.id.nestedScrollView);
        //nestedScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);


        monthViewPager.initDateAndAdapter(BaseCalendarPager.MONTH_VIEW);
        weekViewPager.initDateAndAdapter(BaseCalendarPager.WEEK_VIEW);

        monthViewPager.setTitleDayChange(new BaseCalendarPager.TitleDayChange() {
            @Override
            public void dayChange(DateInfo dateInfo) {
                if(null!=titleDayChange){
                    titleDayChange.dayChange(dateInfo);
                }
            }
        });

        monthViewPager.setSelectedDayListener(new BaseCalendarPager.SelectedDayListener() {
            @Override
            public void selectedDay(DateInfo dateInfo,int x,int y) {
                //Log.e("selsectdate",dateInfo.getSolarYear()+" "+dateInfo.getSolarMonth()
                //+" "+dateInfo.getSolarDay());
                if(isLinkage_WeekControlMonth){
                    isLinkage_WeekControlMonth=false;
                    type_linkage=0;
                }else {
                    pagerLinkage_MonthControlWeek(LINKAGE_SELECTDE_DAY,dateInfo,x,y);
                }


            }
        });

        monthViewPager.setPagerChangeListener(new BaseCalendarPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {
                //Log.e("pager",position+" ");
                if(isLinkage_WeekControlMonth){
                    //isLinkage_MonthControlWeek=true;
                    //pagerLinkage_MonthControlWeek();
                    //Log.e("jjjj","sdwxw");

                    if(type_linkage==LINKAGE_SELECTDE_DAY){
                        DateInfo dateInfo1= monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                                .get(monthViewPager.getCurrentItem()).getList().get(0);
                        DateInfo dateInfo2=weekViewPager.getBaseAdapter().getDateInfo_selectedDay();
                        int days= DateUtils.getDaysBetweenTwoDay(new LocalDate(dateInfo1.getSolarYear()
                                        ,dateInfo1.getSolarMonth(), dateInfo1.getSolarDay()),
                                new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                                        dateInfo2.getSolarDay()));

                        monthViewPager.getBaseAdapter().
                                setInvisibleFlagDay(weekViewPager.getBaseAdapter().getInvisibleFlagDateInfo(),
                                        days/7,days%7);
                        //monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager
                        //        .getCurrentItem()).refreshSelectedDay(days/7,days%7);
                        isLinkage_WeekControlMonth=false;
                        type_linkage = 0;

                    }else {

                        DateInfo dateInfo1 = weekViewPager.getBaseAdapter().mBaseCalendarViewList
                                .get(weekViewPager.getCurrentItem()).getList().get(6);

                        DateInfo dateInfo = new DateInfo();
                        if(dateInfo1.getSolarDay()<7){

                            dateInfo = weekViewPager.getBaseAdapter().mBaseCalendarViewList
                                    .get(weekViewPager.getCurrentItem()).getList().get(7-dateInfo1.getSolarDay());
                        }else {
                            dateInfo = weekViewPager.getBaseAdapter().mBaseCalendarViewList
                                    .get(weekViewPager.getCurrentItem()).getList().get(0);
                        }


                        DateInfo dateInfo2 = weekViewPager.getBaseAdapter().getInvisibleFlagDateInfo();
                        DateInfo dateInfo3 = monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                                .get(monthViewPager.getCurrentItem()).getList().get(0);
                        LocalDate localDate = new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                                dateInfo2.getSolarDay());

                        int days=DateUtils.getDaysBetweenTwoDay(new LocalDate(dateInfo3.getSolarYear(),dateInfo3.getSolarMonth(),
                                dateInfo3.getSolarDay()),new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                                dateInfo2.getSolarDay()));


                        monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo2,days/7,days%7);
                        //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
                        isLinkage_WeekControlMonth=false;
                        type_linkage=0;

                    }



                }else {


                    List<DateInfo> list = monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                            .get(monthViewPager.getCurrentItem()).getList();
                    DateInfo dateInfo = new DateInfo();
                    LocalDate localDate = new LocalDate();
                    int x=-1;
                    int y=-1;

                    for(int i=0;i<list.size();i++){
                        dateInfo = list.get(i);
                        if(dateInfo.getTypeOfMonth()==0){
                            if(monthViewPager.getCurrentItem()==monthViewPager.getmCurrPager()){

                                //Log.e("asaa","aaa");
                                if(dateInfo.getSolarDay()==localDate.getDayOfMonth()){
                                    x=i/7;
                                    y=i%7;
                                    break;
                                }
                                continue;
                            }
                            x=0;
                            y=i;
                            break;
                        }
                    }
                    /**
                    for(DateInfo dateInfo1:list){
                        if(dateInfo1.getTypeOfMonth()==0){
                            if(monthViewPager.getCurrentItem()==monthViewPager.getmCurrPager()){

                                //Log.e("asaa","aaa");
                                if(dateInfo1.getSolarDay()==localDate.getDayOfMonth()){
                                    dateInfo=dateInfo1;
                                    break;
                                }
                                continue;
                            }
                            dateInfo=dateInfo1;
                            break;
                        }
                    }
                     **/

                    monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,x,y);
                    //weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
                    pagerLinkage_MonthControlWeek(LINKAGE_PAGER_CHANGE,dateInfo,-1,y);
                }

            }
        });

        monthViewPager.setTranslationYListener(new BaseCalendarPager.TranslationYListener() {
            @Override
            public void translationY(float offSet) {
                updateHexCalendarViewHeight((int) offSet,false,0);
            }
        });
        weekViewPager.setTitleDayChange(new BaseCalendarPager.TitleDayChange() {
            @Override
            public void dayChange(DateInfo dateInfo) {
                if(null!=titleDayChange){
                    //titleDayChange.dayChange(dateInfo);
                }
            }
        });
        weekViewPager.setSelectedDayListener(new BaseCalendarPager.SelectedDayListener() {

            @Override
            public void selectedDay(DateInfo dateInfo,int x,int y) {
                //Log.e("wwwwpa","ddd");
                if(isLinkage_MonthControlWeek){
                    isLinkage_MonthControlWeek=false;
                    type_linkage=0;
                }else {
                    //weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
                    //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
                    pagerLinkage_WeekControlMonth(LINKAGE_SELECTDE_DAY,dateInfo,x,y);

                }



            }
        });

        weekViewPager.setPagerChangeListener(new BaseCalendarPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {
                if(isLinkage_MonthControlWeek){

                    if(type_linkage==LINKAGE_SELECTDE_DAY){
                        DateInfo dateInfo1 = monthViewPager.getBaseAdapter().getInvisibleFlagDateInfo();
                        LocalDate localDate =  new LocalDate(dateInfo1.getSolarYear()
                                ,dateInfo1.getSolarMonth(), dateInfo1.getSolarDay());
                        int y=localDate.getDayOfWeek();
                        if(y==7) y=0;
                        //weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        //        .get(weekViewPager.getCurrentItem()).refreshSelectedDay(0,y);
                        weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo1,0,y);
                        isLinkage_MonthControlWeek=false;
                        type_linkage = 0;
                    }else {

                        DateInfo dateInfo1 = monthViewPager.getBaseAdapter().getInvisibleFlagDateInfo();
                        LocalDate localDate =  new LocalDate(dateInfo1.getSolarYear()
                                ,dateInfo1.getSolarMonth(), dateInfo1.getSolarDay());
                        int y=localDate.getDayOfWeek();
                        if(y==7) y=0;
                        //weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        //        .get(weekViewPager.getCurrentItem()).refreshSelectedDay(0,y);
                        weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo1,0,y);
                        isLinkage_MonthControlWeek=false;
                        type_linkage=0;

                    }

                }else {




                    List<DateInfo> list = weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                            .get(weekViewPager.getCurrentItem()).getList();

                    DateInfo dateInfo1=list.get(0);
                    DateInfo dateInfo2=list.get(6);
                    DateInfo dateInfo = new DateInfo();

                    if(dateInfo1.getSolarYear()!=dateInfo2.getSolarYear()||
                            dateInfo1.getSolarMonth()!=dateInfo2.getSolarMonth()){
                        dateInfo = XLeoneLunar.getDateInfo(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                                1);
                    }else {
                        dateInfo = dateInfo1;
                    }
                    LocalDate localDate = new LocalDate();

                    for(DateInfo dateInfo3:list){
                        if(dateInfo3.getSolarYear()==localDate.getYear()&&
                                dateInfo3.getSolarMonth()==localDate.getMonthOfYear()&&dateInfo3.getSolarDay()==
                                localDate.getDayOfMonth()){
                            dateInfo=dateInfo3;
                            break;
                        }
                    }

                    LocalDate select = new LocalDate(dateInfo.getSolarYear(),dateInfo.getSolarMonth()
                            ,dateInfo.getSolarDay());
                    int y = select.getDayOfWeek();
                    if(y==7) y=0;

                    //Log.e("dateinfo",dateInfo.getSolarYear()+" "+dateInfo.getSolarMonth()
                    //        +" "+dateInfo.getSolarDay());

                    //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
                    pagerLinkage_WeekControlMonth(LINKAGE_PAGER_CHANGE,dateInfo,0,y);
                }
            }
        });



        //monthViewPager.setVisibility(VISIBLE);
        //weekViewPager.setVisibility(VISIBLE);

        addView(root,0);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                monthViewPager.setVisibility(VISIBLE);
                weekViewPager.setVisibility(GONE);
            }
        },500);

    }
    private void updateHexCalendarViewHeight(int offset,boolean isChangeToLarge,float value){
        ViewGroup.LayoutParams params = hexCalendarView.getLayoutParams();
        params.height = offset;

        hexCalendarView.setLayoutParams(params);
        if(monthViewPager.getVisibility()==GONE){

            float value1 = 5.0f;
            if(!isChangeToLarge){
                value1 = 6.0f;
            }
            //Log.e("wwwwww",offset+" "+getNestedTranY());
            float a=getNestedTranY() +weekPagerHeight * value-offset;

            //Log.e("wwwwww",offset+" "+getNestedTranY()+"  "+a);
            nestedScrollView.setTranslationY(a);
        }

        //Log.e("dcdcdcd",weekViewPager.getHeight()+" ");
    }

    private float startY=0;

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public static int LINKAGE_SELECTDE_DAY=1;
    public static int LINKAGE_PAGER_CHANGE=2;

    public int type_linkage=0;

    public void pagerLinkage_MonthControlWeek(int type_linkage,DateInfo dateInfo,int x,int y){
        isLinkage_MonthControlWeek=true;
        this.type_linkage=type_linkage;
        if(type_linkage==LINKAGE_SELECTDE_DAY){
            monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,x,y);
            LocalDate selectDate = new LocalDate(dateInfo.getSolarYear(),dateInfo.getSolarMonth(),
                    dateInfo.getSolarDay());
            int days = selectDate.getDayOfWeek();
            if(days==7){
                days=0;
            }
            selectDate=selectDate.plusDays(-days);
            DateInfo dateInfo2  =weekViewPager.getBaseAdapter().getmBaseCalendarViewList().
                    get(weekViewPager.getCurrentItem()).getList().get(0);

            int weeks= DateUtils.getWeeksBetweenTwoDay(selectDate,new LocalDate(
                    dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(), dateInfo2.getSolarDay()));
            weeks = (-1)*weeks+weekViewPager.getCurrentItem();
            if(weeks==weekViewPager.getCurrentItem()){
                int y1=selectDate.plusDays(days).getDayOfWeek();
                if(y1==7) y1=0;

                weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,0,y1);

                isLinkage_MonthControlWeek = false;
                type_linkage = 0;
                //weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                //        .get(weekViewPager.getCurrentItem()).refreshSelectedDay(0,y1);

                return;
            }
            weekViewPager.setCurrentItem(weeks,false);
        }else {


            LocalDate selectDate;
            if(monthViewPager.getCurrentItem()==monthViewPager.getmCurrPager()){
                selectDate = new LocalDate();
            }else {
                DateInfo dateInfo1 = monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        .get(monthViewPager.getCurrentItem()).getList().get(0);

                selectDate = new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                        dateInfo1.getSolarDay());
            }

            int days = selectDate.getDayOfWeek();
            if(days==7){
                days=0;
            }
            selectDate=selectDate.plusDays(-days);


            DateInfo dateInfo2  =weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                    .get(weekViewPager.getCurrentItem()).getList().get(0);

            int weeks=DateUtils.getWeeksBetweenTwoDay(selectDate,new LocalDate(dateInfo2.getSolarYear(),dateInfo2.getSolarMonth(),
                    dateInfo2.getSolarDay()));
            //Log.e("weeks",weeks+"");
            if(weeks==0){
                weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,0,y);
                //weekViewPager.getBaseAdapter().getmBaseCalendarViewList().get(weekViewPager.getCurrentItem())
                //        .cleanSelectedDay();
                isLinkage_MonthControlWeek = false;
                type_linkage = 0;
                return;
            }
            weeks = (-1)*weeks+weekViewPager.getCurrentItem();
            weekViewPager.setCurrentItem(weeks,false);
        }

    }

    public void pagerLinkage_WeekControlMonth(int type_linkage,DateInfo dateInfo,int x,int y){

        isLinkage_WeekControlMonth = true;

        this.type_linkage=type_linkage;

        if(type_linkage==LINKAGE_SELECTDE_DAY){
            weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,x,y);
            //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
            //DateInfo dateInfo1 = weekViewPager.getBaseAdapter().getDateInfo_selectedDay();
            LocalDate selectDate = new LocalDate(dateInfo.getSolarYear(),dateInfo.getSolarMonth(),
                    dateInfo.getSolarDay());

            DateInfo dateInfo2  =monthViewPager.getBaseAdapter().getmBaseCalendarViewList().
                    get(monthViewPager.getCurrentItem()).getList().get(0);

            int days= DateUtils.getDaysBetweenTwoDay(new LocalDate(dateInfo2.getSolarYear()
                    ,dateInfo2.getSolarMonth(), dateInfo2.getSolarDay()),selectDate);
            int type_month=monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                    .getList().get(days).getTypeOfMonth();
            if(type_month==0){
                monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,days/7,days%7);
                //monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                //       .refreshSelectedDay(days/7,days%7);
                isLinkage_WeekControlMonth=false;
                this.type_linkage=0;
            }else {

                startChangeMonthHeight(monthViewPager.getCurrentItem(),monthViewPager.getCurrentItem()+type_month);
                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+type_month,false);
                //monthViewPager.getBaseAdapter().setInvisibleFlagDay(weekViewPager.getBaseAdapter()
                //        .mBaseCalendarViewList.get(weekViewPager.getCurrentItem()).getList().get(0));
                //monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                //        .cleanSelectedDay();
                //isLinkage_WeekControlMonth=false;
                //this.type_linkage=0;
            }

            //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);
            //isLinkage_WeekControlMonth=false;
            //this.type_linkage=0;

        }else {


            weekViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,x,y);

            DateInfo dateInfo1 = monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                    .get(monthViewPager.getCurrentItem()).getList().get(0);

            LocalDate selectDate = new LocalDate(dateInfo1.getSolarYear(),dateInfo1.getSolarMonth(),
                    dateInfo1.getSolarDay());

            //Log.e("selectDate",selectDate.getYear()+" "+selectDate.getMonthOfYear()+" "+selectDate.getDayOfMonth());
            DateInfo dateInfo2  =weekViewPager.getBaseAdapter().getmBaseCalendarViewList()
                    .get(weekViewPager.getCurrentItem()).getList().get(0);
            //Log.e("dateInfo2",dateInfo2.getSolarYear()+" "+dateInfo2.getSolarMonth()+" "+
            //        dateInfo2.getSolarDay());

            int days=DateUtils.getDaysBetweenTwoDay(selectDate,new LocalDate(dateInfo2.getSolarYear()
                    ,dateInfo2.getSolarMonth(), dateInfo2.getSolarDay()));

            int a=monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                    .getWeek_num();
            if(days<0){

                startChangeMonthHeight(monthViewPager.getCurrentItem(),monthViewPager.getCurrentItem()-1);

                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()-1,false);

            }else if(days==(a-1)*7){

                //Log.e("wwddssxsxsxsxsx","xx");
                List<DateInfo> list=monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        .get(monthViewPager.getCurrentItem()).getList();

                DateInfo dateInfo3 = list.get(list.size()-1);
                LocalDate localDate = new LocalDate(dateInfo.getSolarYear(),dateInfo.getSolarMonth(),
                        dateInfo.getSolarDay());
                int b=localDate.getDayOfWeek();
                if(b==7) b=0;
                DateInfo dateInfo4 = list.get((a-1)*7+b);

                if(dateInfo4.getTypeOfMonth()!=0){
                    startChangeMonthHeight(monthViewPager.getCurrentItem(),monthViewPager.getCurrentItem()+1);

                    monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1,false);

                }else {

                    monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                            .cleanSelectedDay();
                    monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,a-1,y);
                    isLinkage_WeekControlMonth = false;
                    this.type_linkage = 0;

                }


                //MonthView monthView=(MonthView)monthViewPager.getChildAt(monthViewPager.getCurrentItem());
                //monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1,false);


            }else if(days>(a-1)*7){
                startChangeMonthHeight(monthViewPager.getCurrentItem(),monthViewPager.getCurrentItem()+1);

                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1,false);


                /**
                DateInfo dateInfo5 = weekViewPager.getBaseAdapter()
                        .mBaseCalendarViewList.get(weekViewPager.getCurrentItem()).getList().get(0);
                //monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo);

                //monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                //        .cleanSelectedDay();
                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1,false);

                 **/

            }else if(days==0) {

                List<DateInfo> list=monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        .get(monthViewPager.getCurrentItem()).getList();

                LocalDate localDate = new LocalDate(dateInfo.getSolarYear(),dateInfo.getSolarMonth(),
                        dateInfo.getSolarDay());
                int b=localDate.getDayOfWeek();
                if(b==7) b=0;
                DateInfo dateInfo4 = list.get(b);

                if(dateInfo4.getTypeOfMonth()!=0){
                    startChangeMonthHeight(monthViewPager.getCurrentItem(),monthViewPager.getCurrentItem()-1);
                    monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()-1,false);

                }else {
                    monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,0,b);
                    isLinkage_WeekControlMonth = false;
                    this.type_linkage = 0;

                }

            } else {
                //Log.e("clean","a");

                monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(monthViewPager.getCurrentItem())
                        .cleanSelectedDay();
                monthViewPager.getBaseAdapter().setInvisibleFlagDay(dateInfo,days/7,y);
                isLinkage_WeekControlMonth=false;
                this.type_linkage = 0;
            }
        }

    }

    private float offSet;

    public void setOffSet(float offSet) {
        this.offSet = offSet;
    }

    private void startChangeMonthHeight(int startPosition, int endPosition) {
        final int now = monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(startPosition).getWeek_num();
        int next = monthViewPager.getBaseAdapter().getmBaseCalendarViewList().get(endPosition).getWeek_num();
        if(now==next) return;

        //updateHexCalendarViewHeight(next*weekPagerHeight);


        final boolean isChangeToLarge ;
        if(now<next){
            isChangeToLarge = true;
        }else {
            isChangeToLarge = false;
        }


        isAnimating = true;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "offSet", now, next);
        objectAnimator.setDuration(100);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offSet = (Float) animation.getAnimatedValue();

                updateHexCalendarViewHeight((int) (offSet*weekPagerHeight),isChangeToLarge,now);
                //shrink(tranY,type);
                //.setTranslationY(mViewPagerTranslateY * percent);
                //isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                //monthViewPager.setVisibility(VISIBLE);
                //weekViewPager.setVisibility(GONE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


                setNestedTranY(nestedScrollView.getTranslationY());
                isAnimating = false;


            }
        });
        objectAnimator.start();




        //int height = (now-(now-next)*(1-positionOffset))*weekPagerHeight;
    }

    private int shrinkHeight1,shrinkHeight2,monthPagerHeight,weekPagerHeight;

    public int getShrinkHeight1() {
        //Log.e("vvvv",monthViewPager.getInvisibleFlagDay_Y(monthViewPager.getCurrentItem())+" ");

        return monthViewPager.getInvisibleFlagDay_Y(monthViewPager.getCurrentItem())*weekPagerHeight;
    }

    public int getShrinkHeight2() {
        return monthViewPager.getHeight()-getShrinkHeight1()-weekPagerHeight;
    }

    public int getTotalShrink() {
        return (monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                .get(monthViewPager.getCurrentItem()).getWeek_num()-1)*weekPagerHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        final ViewGroup.LayoutParams params1 = nestedScrollView.getLayoutParams();
        params1.height = getMeasuredHeight()-DpToPx.dpToPx(mContext,50);
        nestedScrollView.setLayoutParams(params1);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //monthViewPager.setVisibility(VISIBLE);
        //weekViewPager.setVisibility(VISIBLE);
        //monthPagerHeight = monthViewPager.getHeight();

        weekPagerHeight = weekViewPager.getHeight();





    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {



        return super.dispatchTouchEvent(ev);
    }


    private float downX,downY;
    private float mLastX,mLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(isAnimating)
        {
            return true;
        }


        final int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                mLastX = downX = x;
                mLastY = downY = y;
                //Log.e("action_Down",downX+" ");
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;


                if(Math.abs(dx)>Math.abs(dy)&&dx!=0&&y<monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        .get(monthViewPager.getCurrentItem()).getWeek_num()*weekPagerHeight){
                    return false;
                }

                /**

                if(dx!=0&& y<monthViewPager.getBaseAdapter().getmBaseCalendarViewList()
                        .get(monthViewPager.getCurrentItem()).getWeek_num()*weekPagerHeight){
                    //Log.e("action_move",dx+" ");
                    return false;
                }
                 **/


                 /*
                   如果向上滚动，且ViewPager已经收缩，不拦截事件
                 */


                if (dy < 0 && nestedScrollView.getTranslationY() <= -getTotalShrink()) {
                    Log.e("tttt","ded");
                    return false;
                }

                //Log.e("dy",nestedScrollView.getScrollY()+" ");
                if (dy > 0 && nestedScrollView.getTranslationY() <= -getTotalShrink()
                        &&nestedScrollView.getScrollY()!=0) {
                    return false;
                }

                if (Math.abs(dy) > mTouchSlop) {
                    if ((dy > 0 && nestedScrollView.getTranslationY() <= 0)
                            || (dy < 0 && nestedScrollView.getTranslationY() >= -getTotalShrink())) {
                        mLastY = y;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(isAnimating)
        {
            return true;
        }
        int action = event.getAction();
        float y = event.getY();
        float x = event.getX();
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = downY = y;
                mLastX = downX = x;
                //return true;
                break;
            case MotionEvent.ACTION_MOVE:
                int totalShrink=getTotalShrink();
                //Log.e("klklklk",nestedScrollView.getTranslationY()+" ");

                float dy = y - mLastY;

                float dx = x - mLastX;




                if (dy < 0 && nestedScrollView.getTranslationY() <= -totalShrink) {
                    mLastY = y;
                    //return false;
                }
                //hideWeek(false);



                if (dy > 0 && nestedScrollView.getTranslationY() + dy >= 0) {
                    nestedScrollView.setTranslationY(0);
                    monthViewPager.setTranslationY(0);
                    monthViewPager.setVisibility(VISIBLE);
                    weekViewPager.setVisibility(GONE);
                    setNestedTranY(0);
                    mLastY = y;
                    return super.onTouchEvent(event);
                }


                if (dy < 0 && nestedScrollView.getTranslationY() + dy <= -totalShrink) {
                    nestedScrollView.setTranslationY(-totalShrink);
                    monthViewPager.setTranslationY(-getShrinkHeight1());
                    setNestedTranY(-totalShrink);
                    monthViewPager.setVisibility(GONE);
                    weekViewPager.setVisibility(VISIBLE);
                    mLastY = y;
                    return super.onTouchEvent(event);
                }


                int type = 0;
                if(dy<0){
                    type=SHRINK;
                }else {
                    type=OPEN;
                }
                shrink(nestedScrollView.getTranslationY()+dy,type);



                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:


                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                float mYVelocity = velocityTracker.getYVelocity();
                //Log.e("azazazazaz",mYVelocity+" "+mMaximumVelocity);
                if (nestedScrollView.getTranslationY() == 0
                        || nestedScrollView.getTranslationY() == getTotalShrink()) {
                    //expand();
                    break;
                }
                if (Math.abs(mYVelocity) >= 800) {
                    if (mYVelocity < 0) {
                        shrinkMonthPager(SHRINK);
                    } else {
                        shrinkMonthPager(OPEN);
                    }
                    return super.onTouchEvent(event);
                }
                if (event.getY() - downY > 0) {
                    shrinkMonthPager(OPEN);
                } else {
                    shrinkMonthPager(SHRINK);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void shrink(float tranY,int type){
        monthViewPager.setVisibility(VISIBLE);
        weekViewPager.setVisibility(GONE);

        float lastNesTrans=nestedScrollView.getTranslationY();
        //Log.e("tranY",tranY+" "+lastNesTrans);
        //float tranY=lastNesTrans+dy;
        int shrinkHeight1=-getShrinkHeight1();
        //Log.e("tranY",shrinkHeight1+" "+getTotalShrink()+" "+hexCalendarView.getHeight());
        int shrinkHeight2=-getShrinkHeight2();
        nestedScrollView.setTranslationY(tranY);
        float aa= tranY*1.0f/-getTotalShrink();
        monthViewPager.setTranslationY(aa*shrinkHeight1);



        //Log.e("0000",0000+" "+tranY+" "+shrinkHeight1);

        //if(type==SHRINK){
        /**
            if(tranY>shrinkHeight1){
                //Log.e("11111",11111+"");
                nestedScrollView.setTranslationY(tranY);
                monthViewPager.setTranslationY(tranY);
            } else if(tranY<=shrinkHeight1&&lastNesTrans>shrinkHeight1){
                //Log.e("22222",222+"");
                nestedScrollView.setTranslationY(shrinkHeight1);
                monthViewPager.setTranslationY(shrinkHeight1);
            }else if(tranY<=shrinkHeight1&&lastNesTrans<=shrinkHeight1&&tranY>-getTotalShrink()){
                //Log.e("3333",3333+"");
                nestedScrollView.setTranslationY(tranY);
            }
        //}else {
            //if(tranY<-getTotalShrink()-shrinkHeight1)
        //}
         **/

    }

    private boolean isAnimating=false;

    private float tranY;

    public void setTranY(float tranY) {
        this.tranY = tranY;
    }

    public static int SHRINK=1;
    public static int OPEN=2;

    public boolean isShrink =false;

    public void shrinkMonthPager(final int type){
        int finishValue = 0;
        if(type==SHRINK)
            finishValue=-getTotalShrink();
        if(isAnimating) return;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "tranY", nestedScrollView.getTranslationY(), finishValue);
        objectAnimator.setDuration(300);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float tranY = (Float) animation.getAnimatedValue();

                shrink(tranY,type);
                //.setTranslationY(mViewPagerTranslateY * percent);
                isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                monthViewPager.setVisibility(VISIBLE);
                weekViewPager.setVisibility(GONE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;

                if(type==SHRINK){
                    setNestedTranY(-getTotalShrink());
                    monthViewPager.setVisibility(GONE);
                    weekViewPager.setVisibility(VISIBLE);
                }else {
                    setNestedTranY(0);
                    monthViewPager.setVisibility(VISIBLE);
                    weekViewPager.setVisibility(GONE);
                }

                setNestedTranY(nestedScrollView.getTranslationY());


            }
        });
        objectAnimator.start();
    }



    private float nestedTranY=0;

    public float getNestedTranY() {
        return nestedTranY;
    }

    public void setNestedTranY(float nestedTranY) {
        this.nestedTranY = nestedTranY;
    }

    private TitleDayChange titleDayChange = null;

    public void setTitleDayChange(TitleDayChange titleDayChange) {
        this.titleDayChange = titleDayChange;
    }

    public interface TitleDayChange{
        void dayChange(DateInfo dateInfo);
    }

}
