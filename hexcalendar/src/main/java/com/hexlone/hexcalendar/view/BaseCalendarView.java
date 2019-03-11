package com.hexlone.hexcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hexlone.hexcalendar.R;
import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;
import com.hexlone.hexcalendar.utils.DpToPx;
import com.hexlone.hexcalendar.utils.MyColor;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public  class BaseCalendarView extends ViewGroup {


    public static int MONTH_VIEW=1;
    public static int WEEK_VIEW=2;

    private int columns = 7;
    private int week_num = 6;

    public int viewWidth,viewHeight,everyDayWidth,everyDayHight;

    private List<DateInfo> list = new ArrayList<>();
    private boolean isHaveDate = false;

    private AttributeSet attributeSet = null;
    private Context mContext;

    private SelectedDayListener selectedDayListener = null;


    public BaseCalendarView(Context context) {
        super(context);
        init(context,null);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public int type=MONTH_VIEW;
    public void setTypeOfView(int d){
        type=d;
    }
    public int getTypeOfView(){
        return type;
    }

    public int getWeek_num() {
        return week_num;
    }


    private void init(Context context,AttributeSet attrs) {
        this.mContext = context;
        this.attributeSet = attrs;
        setWillNotDraw(false);
        list = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        viewWidth = getWidth();
        viewHeight = getHeight();


        everyDayWidth = viewWidth/columns;
        everyDayHight = viewHeight/6;
        if(type==WEEK_VIEW) everyDayHight = viewHeight;
        if(isHaveDate){

            for(int i = 0;i<week_num;i++){

                for(int j=0;j<columns;j++){
                    View child = getChildAt(i*7+j);
                    child.layout(j*everyDayWidth,i*everyDayHight,
                            (j+1)*everyDayWidth,(i+1)*everyDayHight);
                }
            }
        }
    }


    public List<DateInfo> getList() {
        return list;
    }

    public void setDate(List<DateInfo> list){
        isHaveDate = true;
        this.list = list;
        type=MONTH_VIEW;
        if(list.size()==35){
            week_num=5;
        }else if(list.size()==42) {
            week_num = 6;
        }else if(list.size()==28){
            week_num=4;
        }else if(list.size()==7){
            week_num=1;
            type=WEEK_VIEW;
        }

        for(int i = 0;i<week_num;i++){

            for(int j=0;j<columns;j++){
                DayView dayView = new DayView(mContext,attributeSet,list.get(i*7+j));
                addView(dayView,i*7+j);
            }
        }
        requestLayout();
    }

    public int setNowDate(LocalDate localDate){
        int days=DateUtils.getDaysBetweenTwoDay(new LocalDate(list.get(0).getSolarYear(),
                list.get(0).getSolarMonth(),list.get(0).getSolarDay()),localDate);
        ((DayView)getChildAt(days)).isNowDate(true);
        return days/7;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        Paint paint = new Paint();
        paint.setColor(0xff38AE80);
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public int x_selectedDay=-1,y_selectedDay=-1;

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x= e.getX();
            float y=e.getY();
            int i = (int) (y/everyDayHight);
            int j = (int) (x/everyDayWidth);
            if(i>=week_num){
                return true;
            }

            DayView view = (DayView) getChildAt(i*7+j);
            if(type==MONTH_VIEW&&view.getDateInfo().getTypeOfMonth()!=0) return true;

            if(null!=selectedDayListener){

                selectedDayListener.selectedDay(view.getDateInfo(),i,j);

            }


            return true;
        }
    });

    public boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void refreshSelectedDay(int x, int y){





        Log.e("wsss","sasasasa  "+x+" "+y);
        if(x_selectedDay==-1&&y_selectedDay==-1){

        }else {
            DayView dayView = (DayView) getChildAt(x_selectedDay*7+y_selectedDay);
            dayView.isSelectDay(false);
        }
        x_selectedDay=x;
        y_selectedDay=y;
        DayView view = (DayView) getChildAt(x*7+y);
        view.isSelectDay(true);
        isSelected=true;
    }

    public void cleanSelectedDay(){
        Log.e("llllll","sasasasa  ");
        if(x_selectedDay==-1&&y_selectedDay==-1){

        }else {
            DayView dayView = (DayView) getChildAt(x_selectedDay*7+y_selectedDay);
            dayView.isSelectDay(false);
            x_selectedDay=-1;
            y_selectedDay=-1;
        }
        isSelected=false;
    }

    public void setSelectedDayListener(SelectedDayListener selectedDayListener) {
        this.selectedDayListener = selectedDayListener;
    }

    public interface SelectedDayListener{
        void selectedDay(DateInfo dateInfo,int x,int y);
    }

}
