package com.ronda.zhbj.test;

import android.os.health.HealthStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ronda.zhbj.R;
import com.socks.library.KLog;


/**
 * 测试宽、高、margin(left/right/top/bottom), padding
 */
public class Test1Activity extends AppCompatActivity implements View.OnClickListener {

    private Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        view = (Button) findViewById(R.id.button);
//        view.measure(0, 0);
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.setOnClickListener(this);


//        KLog.d("onCreate: " + view.getWidth() + ", " + view.getLeft() + ", " + view.getPaddingLeft());
//        KLog.e("onCreate: " + view.getMeasuredWidth()+", "+ view.getMeasuredHeight());


    }

    @Override
    protected void onStart() {
        super.onStart();
//        KLog.d("onStart: " + view.getWidth() + ", " + view.getLeft() + ", " + view.getPaddingLeft());


    }

    @Override
    protected void onResume() {
        super.onResume();
//        KLog.d("onResume: " + view.getWidth() + ", " + view.getLeft() + ", " + view.getPaddingLeft());

//        view.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                int width = view.getWidth();
//                int left = view.getLeft();
//            }
//        }, 300);

//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                int width = view.getWidth();
//                int measureWidth = view.getMeasuredWidth();
//                int left = view.getLeft();
//
//                KLog.i("OnGlobalLayoutListener: "+ width + ", " + measureWidth + ", " + left);
//            }
//        });


    }

    @Override
    public void onClick(View v) {
//        KLog.d("onClick: " + view.getWidth() + ", " + view.getLeft() + ", " + view.getPaddingLeft());
//
//        KLog.e(view.getLeft());

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

        KLog.e(layoutParams);
        layoutParams.leftMargin += 50;

        view.setLayoutParams(layoutParams);
//        view.requestLayout();



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            int width = view.getWidth();
            int measureWidth = view.getMeasuredWidth();
            int left = view.getLeft();

            KLog.i("onWindowFocusChanged: --> hasFocus: " + hasFocus + width + ", " + measureWidth + ", " + left);
        }
        super.onWindowFocusChanged(hasFocus);
    }


}
