package com.ronda.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.base.BaseDetailPager;
import com.ronda.zhbj.base.BasePager;
import com.ronda.zhbj.base.impl.detail.InteractDetailPager;
import com.ronda.zhbj.base.impl.detail.NewsDetailPager;
import com.ronda.zhbj.base.impl.detail.PhotosDetailPager;
import com.ronda.zhbj.base.impl.detail.TopicDetailPager;
import com.ronda.zhbj.bean.NewsMenu;
import com.ronda.zhbj.http.Api;
import com.ronda.zhbj.utils.CacheUtils;
import com.socks.library.KLog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/14
 * Version: v1.0
 *
 * 新闻中心
 */

public class NewsCenterPager extends BasePager {

    private List<BaseDetailPager> mDetailPagerList;
    private NewsMenu mNewsMenuData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        KLog.i("news center Pager initData");

        // 初始化标题
        ibMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("新闻中心");

        getData();
    }

    private void getData() {

        // 若发现缓存,则立即解析数据,并且同时请求服务器数据. 让用户感觉数据加载很快
        String cache = CacheUtils.getCache(mActivity, Api.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache);
        }

        requestServerData();
    }

    /**
     * 请求服务器数据
     */
    public void requestServerData() {
        RequestParams params = new RequestParams(Api.CATEGORY_URL);
        //params.addQueryStringParameter("wd", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                KLog.i("result: "+ result);
                parseData(result);
                CacheUtils.putCache(mActivity, Api.CATEGORY_URL, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析服务器数据
     *
     * @param result
     */
    private void parseData(String result) {
        mNewsMenuData = new Gson().fromJson(result, NewsMenu.class);
        KLog.e(mNewsMenuData);

        // 给侧边栏填充分类数据
        ((MainActivity) mActivity).setLeftMenuData(mNewsMenuData.getData());

        // 初始化4个详情页
        mDetailPagerList = new ArrayList<>();
        mDetailPagerList.add(new NewsDetailPager(mActivity));
        mDetailPagerList.add(new TopicDetailPager(mActivity));
        mDetailPagerList.add(new PhotosDetailPager(mActivity));
        mDetailPagerList.add(new InteractDetailPager(mActivity));

        // 手动初始化第一个界面
        setCurDetailPager(0);
    }

    /**
     * 设置详情页(即:更改FrameLayout中的内容)
     * @param position
     */
    public void setCurDetailPager(int position) {

        //更新标题
        tvTitle.setText(mNewsMenuData.getData().get(position).getTitle());

        flContent.removeAllViews();
        flContent.addView(mDetailPagerList.get(position).mRootView);
    }
}
