package com.ronda.zhbj.main.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ronda.zhbj.main.base.BaseMenuPager;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 *
 * 专题详情页
 */

public class TopicMenuPager extends BaseMenuPager {

    public TopicMenuPager(Activity activity) {
        super(activity);
    }


    @Override
    public View createView() {

        TextView textView = new TextView(mActivity);
        textView.setText("专题详情页");
        textView.setTextSize(22f);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
