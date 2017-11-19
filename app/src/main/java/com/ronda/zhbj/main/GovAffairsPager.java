package com.ronda.zhbj.main;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ronda.zhbj.main.base.BasePager;
import com.socks.library.KLog;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/14
 * Version: v1.0
 *
 * 政务
 */

public class GovAffairsPager extends BasePager {

    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        KLog.i("gov affairs Pager initData");

        // 初始化标题
        ibMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("政务");

        //给标签页的内容区域填充内容
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(22f);
        textView.setTextColor(Color.RED);

        flContent.addView(textView);
    }
}
