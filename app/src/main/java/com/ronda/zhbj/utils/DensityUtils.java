package com.ronda.zhbj.utils;

import android.content.Context;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/20
 * Version: v1.0
 */

public class DensityUtils {
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = ((int) (dp * density + 0.5f)); // 0.5的目的是便于四舍五入
        return px;
    }

    public static float px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
