package com.ronda.zhbj.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.R;
import com.ronda.zhbj.base.BasePager;
import com.ronda.zhbj.base.impl.GovAffairsPager;
import com.ronda.zhbj.base.impl.HomePager;
import com.ronda.zhbj.base.impl.NewsCenterPager;
import com.ronda.zhbj.base.impl.SettingsPager;
import com.ronda.zhbj.base.impl.SmartServicePager;

import java.util.ArrayList;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/13
 * Version: v1.0
 */

public class ContentFragment extends BaseFragment {

    private ViewPager mViewPager;
    private ArrayList<BasePager> mPagerList;
    private RadioGroup mRadioGroup;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(0);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void initData() {

        /**
         * ViewPager 的初始化
         */
        mPagerList = new ArrayList<>();

        //添加五个标签页
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingsPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        //mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0, false); //参2:表示是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 其实这个加载数据可以放在mRadioGroup的监听器中, 但是需要每一个分支都要写一遍, 相对麻烦了点.所以就给ViewPager设置一个监听器来加载数据
                mPagerList.get(position).initData();

                if (position == 0 || position == mPagerList.size()-1){
                    ((MainActivity) mActivity).setSlidingMenuEnable(false);
                }
                else {
                    ((MainActivity) mActivity).setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);


        // 手动加载第一页数据. 因为OnPageChangeListener在第一次进入页面时不会回调
        mPagerList.get(0).initData();
        ((MainActivity) mActivity).setSlidingMenuEnable(false);//首页禁用侧边栏

    }

    /**
     * 通知NewsPager切换FrameLayout中的内容(点击了SlidingMenu)
     * @param position
     */
    public void notifyNewsPager(int position) {

        NewsCenterPager pager = (NewsCenterPager) mPagerList.get(1);// NewsPager 新闻中心
        pager.setCurDetailPager(position);

    }

    /**
     * ViewPager的适配器
     */
    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager basePager = mPagerList.get(position);
            container.addView(basePager.mRootView);

            // 受ViewPager 加载机制的影响, instantiateItem()方法会调用多次,把相邻的Pager也会创建出来.
            // 所以若是在这里调用basePager.initData(), 也会加载相邻页面的数据, 造成不必要的流量和性能的浪费. 所以可以在当前Pager选中时初始化当前页面数据
            // basePager.initData(); // Pager中内容区域添加内容

            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
