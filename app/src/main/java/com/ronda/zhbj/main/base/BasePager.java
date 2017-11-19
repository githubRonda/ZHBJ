package com.ronda.zhbj.main.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.R;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/14
 * Version: v1.0
 *
 * 主界面五个标签页的基类: title + Content
 */

public class BasePager {

    public Activity mActivity;

    public ImageButton ibMenu;
    public TextView  tvTitle;
    public ImageButton ibPhotoType;
    public FrameLayout flContent;
    public View mRootView;

    public BasePager(Activity activity) {
        this.mActivity = activity;

        mRootView = createView();
    }

    public View createView(){

        View rootView = View.inflate(mActivity, R.layout.base_pager, null);

        ibMenu = (ImageButton) rootView.findViewById(R.id.ib_menu);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        ibPhotoType = (ImageButton) rootView.findViewById(R.id.ib_photo_type);
        flContent = (FrameLayout) rootView.findViewById(R.id.fl_page_content);

        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mActivity).toggleSlidingMenu();
            }
        });

        return rootView;
    }

    /**
     * initData()作用: 1. 初始化title中的View内容   2. 给Content区域添加View
     */
    public void initData(){

    }
}
