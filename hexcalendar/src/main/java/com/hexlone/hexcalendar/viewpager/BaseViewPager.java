package com.hexlone.hexcalendar.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.hexlone.hexcalendar.view.MonthView;

public abstract class BaseViewPager extends ViewPager {

    protected Context mContext;
    protected SelectedDayListener selectedDayListener = null;

    protected PagerChangeListener pagerChangeListener = null;

    public BaseViewPager(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public BaseViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attributeSet){
        this.mContext = context;
        setWillNotDraw(false);

    }



    @Override
    protected void onDraw(Canvas canvas) {


        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        //Log.e("xcxcxx","sdsxs");
    }


    public void setSelectedDayListener(SelectedDayListener selectedDayListener) {
        this.selectedDayListener = selectedDayListener;
    }

    public interface SelectedDayListener{
        void selectedDay(DateInfo dateInfo);
    }

    public void setPagerChangeListener(PagerChangeListener pagerChangeListener) {
        this.pagerChangeListener = pagerChangeListener;
    }

    public interface PagerChangeListener{
        void pagerChange(int position);
    }
}
