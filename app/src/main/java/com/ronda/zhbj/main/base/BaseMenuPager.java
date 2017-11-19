package com.ronda.zhbj.main.base;

import android.app.Activity;
import android.view.View;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 *
 * 菜单详情页 --> 对应的是侧边栏菜单切换的内容
 * 和BasePager结构类似
 */

public abstract class BaseMenuPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuPager(Activity activity) {
        this.mActivity = activity;
        mRootView = createView();
    }

    public abstract View createView();

    public void initData(){

    }
}
