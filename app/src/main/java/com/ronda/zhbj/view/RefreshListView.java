package com.ronda.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ronda.zhbj.R;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/12
 * Version: v1.0
 * <p>
 * 自定义下拉刷新的控件
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_REFRESH = 0; //下拉刷新状态 默认状态
    private static final int STATE_RELEASE_REFRESH = 1; //释放刷新
    private static final int STATE_REFRESHING = 2; //正在刷新

    private int mCurState = STATE_PULL_REFRESH;

    private View mHeaderView;
    private ImageView mIvArrow;
    private ProgressBar mPb;
    private TextView mTvTitle;
    private TextView mTvTime;
    private int mHeaderViewHeight;
    private int startY = -1;  //按下时的纵坐标. -1表示无效值
    private RotateAnimation rotateUpAnimation;//向上旋转的动画
    private RotateAnimation rotateDownAnimation;//向下旋转的动画
    private View mFooterView;
    private int mFooterViewHeight;


    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeaderView();

        initAmin();

        initFooterView();

        setOnScrollListener(this);
    }


    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.item_header_layout, null);
        mIvArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        mPb = (ProgressBar) mHeaderView.findViewById(R.id.pb);
        mTvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mTvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);

        setCurrentTime();

        mHeaderView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        addHeaderView(mHeaderView);
    }

    private void setCurrentTime() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        mTvTime.setText("最后刷新时间: " + format);
    }

    private void initAmin() {
        rotateUpAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnimation.setDuration(200);
        rotateUpAnimation.setFillAfter(true);


        rotateDownAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnimation.setDuration(200);
        rotateDownAnimation.setFillAfter(true);

    }

    private void initFooterView() {

        mFooterView = View.inflate(getContext(), R.layout.item_footer_list, null);

        mFooterView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

        addFooterView(mFooterView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                // 如果是正在刷新中, 就直接返回,不做处理,从而禁止滑动. 至于返回true还是false, 直接调用父类中的方法来决定
                if (mCurState == STATE_REFRESHING) {
                    return super.onTouchEvent(ev);//或者直接break 也可以
                }

                if (startY == -1) {// 当用户跨控件触摸时, 这时down事件是在其他控件上的, 而ListView上是从move事件开始的
                    startY = (int) ev.getY();
                }

                int dy = (int) (ev.getY() - startY);//计算下拉的偏移量
                //只有当偏移量>0(表示是下拉,或者下拉之后再往回拉) 且 第一个可见的条目的索引是0时，才慢慢显示头布局
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    int paddingTop = -mHeaderViewHeight + dy;
                    mHeaderView.setPadding(0, paddingTop, 0, 0);

                    if (paddingTop <= 0 && mCurState != STATE_PULL_REFRESH) {//头布局完全显示时,切换为释放刷新状态. 第二个判断可以保证只刷新一次，避免频繁刷新
                        //下拉刷新
                        mCurState = STATE_PULL_REFRESH;
                        updateHeaderView();

                        KLog.d("下拉刷新");
                    } else if (paddingTop > 0 && mCurState != STATE_RELEASE_REFRESH) {
                        //释放刷新
                        mCurState = STATE_RELEASE_REFRESH;
                        updateHeaderView();

                        KLog.d("释放刷新");
                    }
                    return true;// 注意: 这里必须要返回true. 因为我们自己给mHeaderView设置了Padding来处理滑动事件,不需要ListView内部再次处理滑动事件, 否则在下拉刷新状态时,松开鼠标回退不到起点位置
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;//重置startY的值

                if (mCurState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                } else if (mCurState == STATE_RELEASE_REFRESH) {
                    mHeaderView.setPadding(0, 0, 0, 0);
                    mCurState = STATE_REFRESHING;
                    updateHeaderView();
                }

                break;
        }
        return super.onTouchEvent(ev);// 必须要调用父类的实现方法,否则就滑动不了ListView了
    }

    private void updateHeaderView() {
        switch (mCurState) {
            case STATE_PULL_REFRESH: // 切换回下拉刷新
                mIvArrow.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.INVISIBLE);
                mIvArrow.startAnimation(rotateDownAnimation);
                mTvTitle.setText("下拉刷新");
                break;
            case STATE_RELEASE_REFRESH:// 切换成释放刷新
                mIvArrow.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.INVISIBLE);
                mIvArrow.startAnimation(rotateUpAnimation);
                mTvTitle.setText("释放刷新");
                break;
            case STATE_REFRESHING:// 刷新中...
                mIvArrow.clearAnimation();// 清除箭头动画,否则无法隐藏
                mIvArrow.setVisibility(View.INVISIBLE);
                mPb.setVisibility(View.VISIBLE);
                mTvTitle.setText("正在刷新...");

                if (mListener != null) {
                    mListener.onRefresh();// 通知调用者, 让其到网络加载更多数据.
                }
                break;
        }
    }

    /**
     * 刷新结束, 头布局恢复初始效果
     */
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {
            //加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0); // 隐藏尾布局。
            isLoadingMore = false;

        } else {
            KLog.d("刷新完毕");
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurState = STATE_PULL_REFRESH;
            mIvArrow.setVisibility(View.VISIBLE);
            mPb.setVisibility(View.INVISIBLE);
            mTvTitle.setText("下拉刷新");

            if (success) {// 只有当刷新成功才更新时间. 理论上这个时间应该持久化保存在本地
                setCurrentTime();
            }
        }

    }

    private boolean isLoadingMore = false;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //若是正在加载更多，则直接返回
        if (isLoadingMore) {
            return;
        }

        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition() == getCount() - 1) {
            KLog.d("开始加载更多");

            isLoadingMore = true;
            mFooterView.setPadding(0, 0, 0, 0);

            setSelection(getCount() - 1);// 滑动到最底部, 直接显示加载更多这个item, 无需再次手动滑动

            if (mListener != null) {
                mListener.onLoadMore();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private RefreshListener mListener;

    public void setOnRefreshListener(RefreshListener listener) {
        this.mListener = listener;
    }

    public interface RefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
