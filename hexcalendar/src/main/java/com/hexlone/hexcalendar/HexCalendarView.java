package com.hexlone.hexcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hexlone.hexcalendar.viewpager.MonthViewPager;
import com.hexlone.hexcalendar.viewpager.WeekViewPager;

public class HexCalendarView extends FrameLayout {

    private Context mContext;

    private MonthViewPager monthViewPager;
    private WeekViewPager weekViewPager;
    private LinearLayout week_column_name;



    public HexCalendarView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public HexCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public HexCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attributeSet){
        this.mContext = context;

        TypedArray attr = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.HexCalendarView,
                0, 0);
        //View week_column_name = inflate(context,R.layout.layout_week_column_name, null);
        //addView(week_column_name);
        //monthViewPager = new MonthViewPager(context,attributeSet);



        //monthViewPager.initDateAndAdapter();
        //addView(monthViewPager);

        //weekViewPager = new WeekViewPager(context,attributeSet);

        //weekViewPager.initDateAndAdapter();

        //addView(weekViewPager);
        //Log.e("uuuu",getResources().getDimension(R.dimen.def_week_colume_name_textsize)+"");

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //monthViewPager.layout(0,0,getWidth(), (int) (getHeight()*0.5f));
        //weekViewPager.layout(0, (int) (getHeight()*0.8f),getWidth(),getHeight());
    }
}
