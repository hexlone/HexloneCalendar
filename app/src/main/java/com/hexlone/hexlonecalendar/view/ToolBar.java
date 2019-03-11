package com.hexlone.hexlonecalendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ToolBar extends ViewGroup {

    private Context mContext;

    public ToolBar(Context context) {
        super(context);
        init(context,null);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attributeSet){
        this.mContext=context;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
