package com.hexlone.hexcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hexlone.hexcalendar.R;
import com.hexlone.hexcalendar.entity.DateInfo;
import com.hexlone.hexcalendar.utils.MyColor;

public class DayView extends View {

    private Context mContext;
    public int viewWidth,viewHeight,everyDayWidth,everyDayHight;
    private int heightOfText;

    protected Paint nowMonthPaint;//当前月份笔
    protected Paint nowMonthLunarPaint;//当前农历月份笔
    protected Paint otherMonthPaint;//当前页面其他月份笔
    protected Paint otherMonthLunarPaint;//当前页面其他农历月份笔


    private DateInfo dateInfo=null;
    private boolean isSelected = false;
    private String lunarText = null;
    private String tag = null;

    private boolean isNowDate = false;

    public DayView(Context context) {
        super(context);
        init(context,null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, DateInfo dateInfo) {
        super(context, attrs);
        this.dateInfo = dateInfo;
        lunarText = dateInfo.getLunarDay();
        if(dateInfo.getLunarTerm()!="") lunarText = dateInfo.getLunarTerm();
        if(dateInfo.getLunarFestival()!="")lunarText = dateInfo.getLunarFestival();
        if(dateInfo.getSolarFestival()!="")lunarText = dateInfo.getSolarFestival();

        init(context,attrs);
    }


    public DateInfo getDateInfo() {
        return dateInfo;
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        nowMonthPaint = new Paint();
        nowMonthLunarPaint = new Paint();
        otherMonthPaint = new Paint();
        otherMonthLunarPaint = new Paint();


        nowMonthPaint.setAntiAlias(true);
        nowMonthPaint.setTextAlign(Paint.Align.CENTER);
        nowMonthPaint.setColor(MyColor.NOW_MONTH_DATE);
        nowMonthPaint.setFakeBoldText(true);
        nowMonthPaint.setTextSize(getResources().getDimension(R.dimen.def_day_textsize));

        nowMonthLunarPaint.setAntiAlias(true);
        nowMonthLunarPaint.setTextAlign(Paint.Align.CENTER);
        nowMonthLunarPaint.setColor(MyColor.NOW_MONTH_DATE);
        nowMonthLunarPaint.setFakeBoldText(true);
        nowMonthLunarPaint.setTextSize((getResources().getDimension(R.dimen.def_day_textsize))*0.6f);

        otherMonthPaint.setAntiAlias(true);
        otherMonthPaint.setTextAlign(Paint.Align.CENTER);
        otherMonthPaint.setColor(MyColor.NO_NOW_MONTH_DATE);
        otherMonthPaint.setFakeBoldText(true);
        otherMonthPaint.setTextSize(getResources().getDimension(R.dimen.def_day_textsize));

        otherMonthLunarPaint.setAntiAlias(true);
        otherMonthLunarPaint.setTextAlign(Paint.Align.CENTER);

        Rect rect1 = new Rect();
        nowMonthPaint.getTextBounds("31", 0, 1, rect1);
        heightOfText = rect1.height();


    }

    public void isSelectDay(Boolean b){
        isSelected = b;

        invalidate();
    }

    public boolean isSelected() {

        return isSelected;
    }

    public boolean isInvisibleFlag = false;

    public void setInvisibleFlagBG(){
        if(isSelected) return;




    }

    public void isHaveTag(String tag){
        this.tag = tag;
        invalidate();
    }

    public void isNowDate(boolean isNowDate) {
        this.isNowDate = isNowDate;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        viewWidth = getWidth();
        viewHeight = getHeight();



        if(dateInfo==null) return;

        if(dateInfo.getTypeOfMonth()==0){
            nowMonthPaint.setColor(MyColor.NOW_MONTH_DATE);
            nowMonthLunarPaint.setColor(MyColor.NOW_MONTH_DATE);
        }else {
            nowMonthPaint.setColor(MyColor.NO_NOW_MONTH_DATE);
            nowMonthLunarPaint.setColor(MyColor.NO_NOW_MONTH_DATE);
        }

        if(isNowDate){
            nowMonthPaint.setColor(MyColor.NOW_DAY_DATE);
            nowMonthLunarPaint.setColor(MyColor.NOW_DAY_DATE);
        }

        if(isSelected) {
            nowMonthPaint.setColor(MyColor.SELECTED_TEXT);
            nowMonthLunarPaint.setColor(MyColor.SELECTED_TEXT);
            Paint bgPaint = new Paint();
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(MyColor.SELECTED_BG);

            bgPaint.setAntiAlias(true);
            canvas.drawCircle(viewWidth/2,viewHeight/2,
                    (viewWidth>viewHeight?viewHeight:viewWidth)/2,bgPaint);

        }

        canvas.drawText(String.valueOf(dateInfo.getSolarDay()),(viewWidth/2),
                (viewHeight/2),nowMonthPaint);

        canvas.drawText(lunarText,viewWidth/2,
                viewHeight/2+heightOfText,nowMonthLunarPaint);


        /**
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawRect(2,2,viewWidth-2,viewHeight-2,paint);

         **/

        if(null!=tag){
            Paint tagPaint = new Paint();

            tagPaint.setAntiAlias(true);
            tagPaint.setTextAlign(Paint.Align.RIGHT);
            tagPaint.setTextSize((getResources().getDimension(R.dimen.def_day_textsize))*0.5f);
            Rect rect2 = new Rect();
            tagPaint.getTextBounds("假", 0, 1, rect2);



            if(isSelected){
                tagPaint.setStyle(Paint.Style.FILL);
                tagPaint.setColor(Color.WHITE);
                canvas.drawCircle(viewWidth-rect2.height()-rect2.height()/2,(rect2.height())*1.5f,
                        (rect2.height()>rect2.width()?rect2.width():rect2.height()),tagPaint);
            }


            tagPaint.setColor(MyColor.DAY_TAG);
            canvas.drawText(tag,viewWidth-rect2.height(),rect2.height()*2,tagPaint);
        }

    }


}
