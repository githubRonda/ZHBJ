package com.ronda.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 *
 * 详情页
 * 和BasePager结构类似
 */

public abstract class BaseDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseDetailPager(Activity activity) {
        this.mActivity = activity;
        mRootView = createView();
    }

    public abstract View createView();

    public void initData(){

    }
}
