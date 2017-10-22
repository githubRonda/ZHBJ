package com.ronda.zhbj;

import android.app.Application;

import com.socks.library.KLog;

import org.xutils.x;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/07
 * Version: v1.0
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        init();
    }

    private void init() {
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        KLog.init(true, "Liu");
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
