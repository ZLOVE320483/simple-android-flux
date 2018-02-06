package com.github.android.flux.utils;

import android.content.Context;

/**
 * Created by zlove on 2018/1/10.
 */

public class UIUtils {

    public static float dip2Px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }
}
