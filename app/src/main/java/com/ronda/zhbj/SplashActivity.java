package com.ronda.zhbj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.ronda.zhbj.utils.PrefUtils;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        intAnim();
    }

    private void intAnim() {
        //选转动画(中心旋转360度)
        RotateAnimation rotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(1000);
        rotateAnim.setFillAfter(true);

        //缩放动画(中心缩放，从无到有)
        ScaleAnimation scaleAnim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(1000);
        scaleAnim.setFillAfter(true);

        //渐变动画
        AlphaAnimation alphaAnim = new AlphaAnimation(0, 1f);
        alphaAnim.setDuration(2000);// 渐变动画要多展示1s
        alphaAnim.setFillAfter(true);

        //让动画一起运行
        AnimationSet set = new AnimationSet(true);// true表示共享插补器
        set.addAnimation(rotateAnim);
        set.addAnimation(scaleAnim);
        set.addAnimation(alphaAnim);

        //启动动画
        rlRoot.startAnimation(set);

        //动画监听器
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }


            @Override
            public void onAnimationEnd(Animation animation) {// 动画结束，跳转页面
                // 若是第一次进入，跳转至引导页，否则跳转至主界面

                boolean is_first_enter = PrefUtils.getBoolean(getApplicationContext(), "is_first_enter", true);
                if (is_first_enter){
                    startActivity(new Intent(getApplicationContext(), GuideActivity.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
