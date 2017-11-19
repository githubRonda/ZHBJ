package com.ronda.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/07
 * Version: v1.0
 * Tab 中的顶部的 TopNews 对应的 ViewPager
 * 之所以要复写这个ViewPager是因为这个TopNews对应的 ViewPager 和 Tab内容本身所在的ViewPager的滑动事件是有冲突的
 * 需求: 向左滑动TopNewsViewPager,当滑动到最后一个时,再次左滑, 则Tab跳转到下一个. 同理, 向右滑动时,当滑动到第一个时,再次右滑, 则Tab跳转到上一个.
 */

public class TopNewsViewPager extends ViewPager {
    private int startY;
    private int startX;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 0. 先统一请求父级不要拦截事件. 具体的情况后面在判断
     * 1. 上下滑动,不需要请求父控件不要拦截事件, 因为当前ViewPager不需要处理该事件
     * 2. 向右滑动并且当前是第一个页面,不需要请求父控件不要拦截事件, 因为当前ViewPager不需要处理该事件, 可由Tab对应的ViewPager来处理,从而滑动tab
     * 3. 向左滑动并且当前是最后一个页面,不需要请求父控件不要拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);// 先统一请求父级不要拦截事件. 具体的情况后面在判断

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                int dx = (int) (ev.getX() - startX);
                int dy = (int) (ev.getY() - startY);

                if (Math.abs(dx) > Math.abs(dy)) {

                    if (dx < 0) {
                        //向左滑
                        int count = getAdapter().getCount();
                        if (getCurrentItem() == count - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //向右滑
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
                    //上下滑动. (当前ViewPager不需要处理事件)
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
