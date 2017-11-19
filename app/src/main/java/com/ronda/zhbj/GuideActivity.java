package com.ronda.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ronda.zhbj.utils.DensityUtils;
import com.ronda.zhbj.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手引导页
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button btnStart;
    private LinearLayout llContainer;

    private int[] mImageResIds = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> mImageViewList; //ImageView 集合

    private ImageView ivRedPoint;
    private int mPointDistance; //两个小圆点之间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        btnStart = (Button) findViewById(R.id.btn_start);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);


        initData();
        mViewPager.setAdapter(new GuideAdapter());


        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 滚动时调用
             * @param position   向右滑时，表示当前page的索引，翻页成功时position才会+1； 向左滑时, 表示当前page的索引-1，翻页不成功时position又会变成当前page的索引值。但是position是在[0, count-1]这个范围的
             * @param positionOffset 当前偏移量的比例。范围[0, 1). 从左往右是从0逐渐变为1，从右往左是从1逐渐变为0
             * @param positionOffsetPixels 当前偏移量的具体值。单位px
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //KLog.i("onPageScrolled --> position: " + position + ", positionOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);

                int offset = (int) (position * mPointDistance + mPointDistance * positionOffset);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                layoutParams.leftMargin = offset;
                //这里 setLayoutParams 和 requestLayout 是可以只用写其中一个的, 因为layoutParams本身就是一个引用, 而且 setLayoutParam()内部也调用了 requestLayout() 方法
                ivRedPoint.setLayoutParams(layoutParams);
                //ivRedPoint.requestLayout();
            }

            // 选中时调用
            @Override
            public void onPageSelected(int position) {
                if(position == mImageViewList.size()-1){
                    btnStart.setVisibility(View.VISIBLE);
                }
                else{
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() { //layout方法执行结束的回调
                // 移除监听,避免重复回调. 否则该方法一般会连续回调3次
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);// 这个remove方法在API16时过时改为removeOnGlobalLayoutListener. 估计是google工程师命名时马虎了

                // 计算两个圆点的距离
                // 移动距离=第二个圆点left值 - 第一个圆点left值
                mPointDistance = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrefUtils.putBoolean(getApplicationContext(), "is_first_enter", false);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void initData() {

        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageResIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageResIds[i]); // 设置背景可以会自动填充宽高，而设置前景(setImageResource())就是原图片的大小

            mImageViewList.add(imageView);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置左边距
            if (i > 0) {
                //从第二个点开始设置左边距
                params.leftMargin = DensityUtils.dp2px(this, 10);//10dp的margin
                point.setLayoutParams(params);
            }
            llContainer.addView(point); // 贴容器添加小圆点
        }
    }


    class GuideAdapter extends PagerAdapter {

        //item个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
