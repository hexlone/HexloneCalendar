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

import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.DateUtils;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeekView extends ViewGroup {


    private int columns = 7;

    public int viewWidth,viewHeight,everyDayWidth,everyDayHight;


    private List<DateInfo> list = new ArrayList<>();
    private boolean isHaveDate = false;

    private AttributeSet attributeSet = null;
    private Context mContext;
    private SelectedDayListener selectedDayListener = null;

    public WeekView(Context context) {
        super(context);
        init(context,null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs) {
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
        everyDayHight = viewHeight;
        if(isHaveDate){



            for(int j=0;j<columns;j++){
                View child = getChildAt(j);
                child.layout(j*everyDayWidth,0,
                        (j+1)*everyDayWidth,everyDayHight);

                //Log.e("dwddwdw",j*everyDayWidth+"  "+i*everyDayHight);
            }
        }
    }

    public List<DateInfo> getList() {
        return list;
    }

    public void setDate(List<DateInfo> list){
        isHaveDate = true;
        this.list = list;

        //Log.e("wwwwww",list.size()+" ");
        for(int j=0;j<columns;j++){
            DayView dayView = new DayView(mContext,attributeSet,list.get(j));
            addView(dayView,j);
        }

        requestLayout();
    }

    public void setNowDate(LocalDate localDate){
        int days=DateUtils.getDaysBetweenTwoDay(new LocalDate(list.get(0).getSolarYear(),
                list.get(0).getSolarMonth(),list.get(0).getSolarDay()),localDate);
        //Log.e("rrrr",days+"");
        ((DayView)getChildAt(days)).isNowDate(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        Paint paint = new Paint();
        paint.setColor(0xff38AE80);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x= e.getX();
            float y=e.getY();
            int i = 0;
            int j = (int) (x/everyDayWidth);

            refreshSelectedDay(i,j);
            return true;
        }
    });

    public void refreshSelectedDay(int x,int y){


        for(int j=0;j<columns;j++){

            DayView dayView = (DayView) getChildAt(j);

            dayView.isSelectDay(false);

        }
        DayView view = (DayView) getChildAt(y);
        view.isSelectDay(true);
        if(null!=selectedDayListener){
            selectedDayListener.selectedDay(view.getDateInfo(),x,y);
        }


    }

    public void cleanSelectedDay(){
        for(int j=0;j<columns;j++){

            DayView dayView = (DayView) getChildAt(j);

            dayView.isSelectDay(false);

        }
    }


    public void setSelectedDayListener(SelectedDayListener selectedDayListener) {
        this.selectedDayListener = selectedDayListener;
    }

    public interface SelectedDayListener{
        void selectedDay(DateInfo dateInfo,int x,int y);
    }

}
