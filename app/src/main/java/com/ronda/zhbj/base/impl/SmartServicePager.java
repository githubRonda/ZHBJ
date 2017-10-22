package com.ronda.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ronda.zhbj.base.BasePager;
import com.socks.library.KLog;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/14
 * Version: v1.0
 *
 * 智慧服务
 */

public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        KLog.i("smart service Pager initData");

        // 初始化标题
        ibMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("智慧服务");

        //给标签页的内容区域填充内容
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(22f);
        textView.setTextColor(Color.RED);

        flContent.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
