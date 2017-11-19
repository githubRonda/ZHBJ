package com.ronda.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/14
 * Version: v1.0
 *
 * 自定义的不允许滑动的ViewPager
 */

public class NoScrollViewPager extends ViewPager {


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        return false; // 不拦截子控件的事件.原因:滑动tab时, 会出现划动很慢的情况, 此时就是事件在分发的时候被最外层的ViewPager给拦截掉了
    }


    // 原生的ViewPager之所以可以滑动,肯定是在onTouchEvent()中进行了处理. 所以只要复写了这个方法并且直接返回true, 则ViewPager就滑动不了了
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 重写此方法, 触摸时什么都不做, 从而实现对滑动事件的禁用
        return true;
    }
}
