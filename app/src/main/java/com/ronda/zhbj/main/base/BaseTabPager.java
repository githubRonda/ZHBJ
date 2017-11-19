package com.ronda.zhbj.main.base;

import android.app.Activity;
import android.view.View;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/22
 * Version: v1.0
 *
 * tab 的基类, 和 BaseMenuPager类的结构是一模一样的
 * 对应的是顶部tab切换的内容
 */

public abstract class BaseTabPager {

    public Activity mActivity;
    public View mRootView;

    public BaseTabPager(Activity activity) {
        this.mActivity = activity;
        mRootView = createView();
    }

    public abstract View createView();

    public void initData(){

    }
}
