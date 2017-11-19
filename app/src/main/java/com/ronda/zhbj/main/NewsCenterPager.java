package com.ronda.zhbj.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.R;
import com.ronda.zhbj.bean.NewsMenu;
import com.ronda.zhbj.http.Api;
import com.ronda.zhbj.main.base.BaseMenuPager;
import com.ronda.zhbj.main.base.BasePager;
import com.ronda.zhbj.main.menu.InteractMenuPager;
import com.ronda.zhbj.main.menu.NewsMenuPager;
import com.ronda.zhbj.main.menu.PhotosMenuPager;
import com.ronda.zhbj.main.menu.TopicMenuPager;
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
 * <p>
 * 新闻中心
 */

public class NewsCenterPager extends BasePager {

    private List<BaseMenuPager> mDetailPagerList;
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
        if (!TextUtils.isEmpty(cache)) {
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
                KLog.i("result: " + result);
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
        mDetailPagerList.add(new NewsMenuPager(mActivity, mNewsMenuData.getData().get(0).getChildren()));
        mDetailPagerList.add(new TopicMenuPager(mActivity));
        mDetailPagerList.add(new PhotosMenuPager(mActivity, ibPhotoType)); // 把标题栏中的组图样式切换的View传到PhotosMenuPager中,便于添加事件
        mDetailPagerList.add(new InteractMenuPager(mActivity));

        // 手动初始化第一个界面
        setCurDetailPager(0);
    }

    /**
     * 设置详情页(即:更改FrameLayout中的内容)
     *
     * @param position
     */
    public void setCurDetailPager(int position) {

        //更新标题
        tvTitle.setText(mNewsMenuData.getData().get(position).getTitle());

        flContent.removeAllViews(); // 先清除旧的布局,再添加新的布局
        BaseMenuPager pager = mDetailPagerList.get(position);
        flContent.addView(pager.mRootView);
        pager.initData();

        //若是切换到组图界面, 需要更新标题栏
        if (pager instanceof PhotosMenuPager) {
            //显示标题栏右边按钮
            ibPhotoType.setVisibility(View.VISIBLE);

        } else {
            //隐藏标题栏右边按钮
            ibPhotoType.setVisibility(View.GONE);
        }
    }
}
