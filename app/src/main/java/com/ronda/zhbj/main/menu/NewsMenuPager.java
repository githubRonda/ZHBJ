package com.ronda.zhbj.main.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.R;
import com.ronda.zhbj.bean.NewsMenu;
import com.ronda.zhbj.main.base.BaseMenuPager;
import com.ronda.zhbj.main.tab.TabDetailPager;
import com.socks.library.KLog;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 * <p>
 * 新闻详情页
 */

public class NewsMenuPager extends BaseMenuPager implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.view_pager)
    ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    List<NewsMenu.DataBean.ChildrenBean> mTabDataList;
    List<TabDetailPager> mTabPagerList;

    public NewsMenuPager(Activity activity, List<NewsMenu.DataBean.ChildrenBean> data) {
        super(activity);
        mTabDataList = data;
    }

    @Override
    public View createView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        x.view().inject(this, view);

        return view;
    }


    @Override
    public void initData() {
        mTabPagerList = new ArrayList<>();
        for (int i = 0; i < mTabDataList.size(); i++) {
            TabDetailPager tabPager = new TabDetailPager(mActivity, mTabDataList.get(i));
            mTabPagerList.add(tabPager);
        }

        mViewPager.setAdapter(new NewsMenuDetailAdapter());

        mIndicator.setViewPager(mViewPager);// 将ViewPager与Indicator绑定在一起,注意必须要在viewPager.setAdapter()之后设置才可以,否则会报错

        //给ViewPager设置监听有两种方法:1. ViewPager.addOnPageChangeListener() 2. TabPageIndicator.setOnPageChangeListener()
        // 千万不能使用ViewPager.setOnPageChangeListener(), 因为这个会和 TabPageIndicator 有冲突,导致Indicator的选中效果失效
        mIndicator.setOnPageChangeListener(this);
        //mViewPager.addOnPageChangeListener(this); // 可以使用这种方式
        //mViewPager.setOnPageChangeListener(this); // // 不能使用这种方式

        //第一次进入页面,初始化一下,设置侧边栏可用
        ((MainActivity) mActivity).setSlidingMenuEnable(true);
    }

    @Event(value = {R.id.ib_next})
    private void onClick(View view) {
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    //==================OnPageChangeListener===================
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        KLog.d("当前Tab的position为: " + position);

        if (position == 0) {
            //开启侧边栏
            ((MainActivity) mActivity).setSlidingMenuEnable(true);
        } else {
            //禁用侧边栏
            ((MainActivity) mActivity).setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class NewsMenuDetailAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabDataList.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mTabPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = mTabPagerList.get(position);
            View view = tabDetailPager.mRootView;
            container.addView(view);
            tabDetailPager.initData();
            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
