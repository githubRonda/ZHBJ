package com.ronda.zhbj.base.impl.detail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ronda.zhbj.base.BaseDetailPager;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 *
 * 新闻详情页
 */

public class NewsDetailPager extends BaseDetailPager {

    public NewsDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View createView() {

        TextView textView = new TextView(mActivity);
        textView.setText("新闻详情页");
        textView.setTextSize(22f);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
