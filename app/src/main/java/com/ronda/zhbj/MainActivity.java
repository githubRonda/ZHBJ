package com.ronda.zhbj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ronda.zhbj.bean.NewsMenu;
import com.ronda.zhbj.fragment.ContentFragment;
import com.ronda.zhbj.fragment.LeftMenuFragment;

import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG_LEFT_MENU = "tag_left_menu";
    private static final String TAG_CONTENT = "tag_content";
    private SlidingMenu mSlidingMenu;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        //设置侧边栏从左边拉出
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //设置全屏触摸范围可拉出侧边栏
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置内容区的宽度,而不是侧边栏的宽度
        mSlidingMenu.setBehindOffset(500);

        //设置侧边栏的布局文件
        mSlidingMenu.setMenu(R.layout.left_menu);
        //menu.setSecondaryMenu(R.layout.right_menu);//设置第二个侧边栏(一般是右边)

        initFragment();
    }

    /**
     * 初始化Fragment(侧滑菜单 和 内容区域)
     */
    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();// 开始事务

        /**
         * containerViewId：装载Fragment实例的容器
         * fragment：带添加的Fragment实例
         * tag：为该Fragment实例设置一个tag标识，方便于FragmentManager.findFragmentByTag(String)获取
         */
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.fl_content, new ContentFragment(), TAG_CONTENT);
        transaction.commit();// 提交事务
    }


    /**
     * 是否启用侧边栏
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        if (enable){
            mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    public void setLeftMenuData(List<NewsMenu.DataBean> data){

        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) mFragmentManager.findFragmentByTag(TAG_LEFT_MENU);
        leftMenuFragment.setMenuData(data);
    }

    /**
     * 打开或关闭侧边栏
     */
    public void toggleSlidingMenu() {
        mSlidingMenu.toggle();
    }

    /**
     * 通知新闻中心Pager, 侧边栏中点击了哪一项
     * @param position
     */
    public void notifyLeftMenuClicked(int position) {
        ContentFragment contentFragment= (ContentFragment) mFragmentManager.findFragmentByTag(TAG_CONTENT);
        contentFragment.notifyNewsPager(position);

    }
}
