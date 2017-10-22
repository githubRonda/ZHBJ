package com.ronda.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/12
 * Version: v1.0
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity; //宿主Activity,即MainActivity

    // Fragment创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
    }

    // 初始化fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        return view;
    }


    // fragment所依赖的activity的onCreate方法执行结束
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化数据
        initData();
    }

    // 初始化布局, 必须由子类实现
    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    // 初始化数据, 必须由子类实现
    public abstract void initData();
}
