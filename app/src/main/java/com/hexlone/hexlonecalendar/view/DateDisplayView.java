package com.hexlone.hexlonecalendar.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.hexlone.hexcalendar.utils.DpToPx;

public class DateDisplayView extends View {

    private Context mContext;

    private int heightOfText,viewHeight,viewWidth;

    private Paint textPaint;
    public DateDisplayView(Context context) {
        super(context);
        init(context,null);
    }

    public DateDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DateDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }



    private int  newDate=0;
    private int  nextDate=0;

    private float value;

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    private int leftOrRight;

    public void changeText(int  date){
        this.leftOrRight = leftOrRight;

        if(newDate==0){
            newDate = date;
            nextDate = date;
        }else {
            newDate = nextDate;
            nextDate = date;
            if(newDate==date) return;
            if(newDate<date) leftOrRight = 1;
            if(newDate>date) leftOrRight = -1;
        }


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,"value",0,1);
        objectAnimator.setDuration(150);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }



    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        textPaint = new Paint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(com.hexlone.hexcalendar.R.dimen.display_textsize));
        textPaint.setTextAlign(Paint.Align.CENTER);
        Rect rect1 = new Rect();
        textPaint.getTextBounds("31", 0, 1, rect1);
        heightOfText = rect1.height();
    }

    private int textSize;

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        Rect rect1 = new Rect();
        textPaint.getTextBounds("31", 0, 1, rect1);
        heightOfText = rect1.height();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getWidth();
        viewHeight = getHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {


        if(newDate==0||nextDate==0) return;


        textPaint.setAlpha((int) ((1-value)*255));

        canvas.drawText(newDate+"",viewWidth/2-leftOrRight*value*viewWidth/3,viewHeight/2+heightOfText/2,textPaint);

        textPaint.setAlpha((int) (value*255));

        canvas.drawText(nextDate+"",viewWidth/2+leftOrRight*(1-value)*viewWidth/3,viewHeight/2+heightOfText/2,textPaint);

    }
}
