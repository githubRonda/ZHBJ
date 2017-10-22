package com.ronda.zhbj.base;

import android.app.Activity;
import android.os.Bundle;

import org.xutils.x;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
