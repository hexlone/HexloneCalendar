package com.hexlone.hexcalendar.utils;

import android.content.Context;

/**
 * Created by wl624 on 2017/11/1.
 */

public class DpToPx {
    public static int dpToPx(Context context, float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    public static float dpToPxF(Context context, float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (float)(dpValue*scale);
    }
}
