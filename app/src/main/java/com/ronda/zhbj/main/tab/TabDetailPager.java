package com.ronda.zhbj.main.tab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ronda.zhbj.NewsDetailActivity;
import com.ronda.zhbj.R;
import com.ronda.zhbj.bean.NewsMenu;
import com.ronda.zhbj.bean.TabBean;
import com.ronda.zhbj.http.Api;
import com.ronda.zhbj.main.base.BaseTabPager;
import com.ronda.zhbj.utils.CacheUtils;
import com.ronda.zhbj.utils.MyBitmapUtils;
import com.ronda.zhbj.utils.PrefUtils;
import com.ronda.zhbj.view.RefreshListView;
import com.socks.library.KLog;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/22
 * Version: v1.0
 * <p>
 * 对应每个Tab对象
 */

public class TabDetailPager extends BaseTabPager implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.view_pager)
    ViewPager mViewPager;

    @ViewInject(R.id.tv_title)
    TextView mTvTitle;

    @ViewInject(R.id.indicator)
    CirclePageIndicator mIndicator;

    @ViewInject(R.id.list_view)
    RefreshListView mListView;

    private NewsMenu.DataBean.ChildrenBean mData; // 构造器传递过来的数据
    private List<TabBean.DataBean.TopnewsBean> mTopnewsList; //从服务器获取到的数据
    private String mUrl;
    private final ImageOptions mImageOptions;
    private List<TabBean.DataBean.NewsBean> mNewsList;
    private String mMoreUrl;
    private NewsAdapter mNewsAdapter;

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.DataBean.ChildrenBean dataBean) {
        super(activity); // 父类中会调用createView 方法, 在mData初始化之前执行, 所以createView中不能使用mData, 否则就会空指针异常
        mData = dataBean;
        mUrl = Api.SERVER_URL + mData.getUrl();


        mImageOptions = new ImageOptions.Builder()
                //.setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                //.setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                //.setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.topnews_item_default) // 设置加载中的图片(直接给ImageView用setImageResource()设置一个默认图片是不管用的), 避免图片加载过程中出现白板, 体验不好.
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public View createView() {
        /**
         * createView这个方法中不能使用mData, 因为构造器中是先调用这个方法,然后再给 mData 初始化的
         */
        View view = View.inflate(mActivity, R.layout.pager_tab, null);
        //x.view().inject(view); // 使用这种方式无效
        x.view().inject(this, view);

        // 给listview添加头布局
        View headerView = View.inflate(mActivity, R.layout.header_list, null);
        x.view().inject(this, headerView);// 此处必须将头布局也注入

        mListView.addHeaderView(headerView);

        mListView.setOnRefreshListener(new RefreshListView.RefreshListener() {

            @Override
            public void onRefresh() {
                requestServerData();
            }

            @Override
            public void onLoadMore() {
                KLog.d("onLoadMore");
                if (mMoreUrl != null) {

                    requestMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了...", Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete(true);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * ListView 中 ItemView的点击事件
             * @param parent 指的就是 ListView 本身
             * @param view 点击的这个 ItemView
             * @param position ItemView的索引位置. 如果有头布局的话, 则要从头布局开始算起
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = mListView.getHeaderViewsCount();
                position = position - headerViewsCount;

                KLog.d("点击位置: position: " + position);
                //说明点击的是头布局: 下拉刷新, 此时直接返回. 注意点击头布局ViewPager的话,这里是不会响应到的,因为事件被ViewPager内部给消费掉了
                if (position < 0) {
                    return;
                }

                int news_id = mNewsList.get(position).getId();// 每条新闻的id

                // read_ids: 1101,1102,1105,1203,
                String read_ids = PrefUtils.getString(mActivity, "read_ids", "");
                if (!read_ids.contains(String.valueOf(news_id))) {// 只有不包含当前id,才追加,避免重复添加同一个id

                    read_ids += news_id + ",";
                    PrefUtils.putString(mActivity, "read_ids", read_ids);
                }

                // 要将被点击的item的文字颜色改为灰色, 局部刷新, view对象就是当前被点击的对象
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);
                // mNewsAdapter.notifyDataSetChanged();//全局刷新, 浪费性能

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).getUrl());
                mActivity.startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }

        requestServerData();
    }

    private void requestServerData() {

        RequestParams params = new RequestParams(mUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                KLog.d("tab request data: " + result);
                processData(result, false);
                //缓存数据
                CacheUtils.putCache(mActivity, mUrl, result);

                // 收起下拉刷新控件
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                // 收起下拉刷新控件
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }


    /**
     * 请求下一页数据
     */
    private void requestMoreDataFromServer() {
        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result, true);
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 处理服务器返回的数据
     *
     * @param result
     * @param isMore 用于区分是否是加载更多获取的数据. 若是加载更多获取到的数据,应该追加在原有集合后面,而不是替换掉原有集合中的数据
     */
    private void processData(String result, boolean isMore) {
        TabBean tabBean = new Gson().fromJson(result, TabBean.class);

        String moreUrl = tabBean.getData().getMore();
        if (TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = null;
        } else {
            mMoreUrl = Api.SERVER_URL + moreUrl;
        }

        if (!isMore) { // 不是加载更多获取到的数据
            mTopnewsList = tabBean.getData().getTopnews();

            //头条新闻数据填充
            if (mTopnewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());

                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//以快照方式展示, 切换圆点选中位置时是没有动画的

                // 每次页面重新创建时, 默认让第一个选中(解决当切换tab时,再回到之前的tab时, 发现页面是销毁后重新初始化的,但是indicator仍然是保留上次圆点的位置的bug)
                // 注意:只能使用mIndicator.onPageSelected(0); 不能使用mIndicator.setCurrentItem(0); 因为后面这个方法是不能改变小圆点选中的位置的!!!
                // TabDetailPager 并不是不可见的时候就销毁掉了,由于ViewPager的特性,会保留左右临近的一个pager
                mIndicator.onPageSelected(0);

                // 给title初始化数据, 然后设置事件监听改变title
                mTvTitle.setText(mTopnewsList.get(0).getTitle());
                // 这里要么使用ViewPager.addOnPageChangeListener(), 要么使用CirclePageIndicator.setOnPageChangeListener(), 不能使用这里要么使用ViewPager.setOnPageChangeListener()
                //mViewPager.addOnPageChangeListener(this);//或者
                mIndicator.setOnPageChangeListener(this);

            }

            if (mHandler==null){
                // 保证启动自动轮播逻辑只执行一次
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        int currentItem = mViewPager.getCurrentItem();
                        currentItem ++;
                        if (currentItem > mTopnewsList.size()-1){

                            currentItem = 0;// 如果已经到了最后一个页面,跳到第一页
                        }
                        mViewPager.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 3000); //形成一个循环
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000); //延迟3s发送一个空消息


                //触摸ViewPager时,停止自动轮播
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                // 停止广告自动轮播, 删除handler的所有消息
                                mHandler.removeCallbacksAndMessages(null); // 当形参为null时, 所有的回调和消息都会被移除
                                break;
                            case MotionEvent.ACTION_CANCEL: // 当在ViewPager上产生down事件,然后move到其他控件上时,会产生cancel事件
                                mHandler.sendEmptyMessageDelayed(0, 3000);// 启动广告
                                break;
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0, 3000);// 启动广告
                                break;
                        }

                        return false;
                    }
                });
            }


            //列表新闻数据填充
            mNewsList = tabBean.getData().getNews();
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }
        } else { // 加载更多获取到的数据
            mNewsList.addAll(tabBean.getData().getNews());// 将数据追加在原来的集合中
            mNewsAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //更新新闻标题
        mTvTitle.setText(mTopnewsList.get(position).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTopnewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(mActivity);

            // 下载图片-将图片设置给imageview-避免内存溢出-缓存 (使用XUtils -- Image 模块)
            x.image().bind(imageView, mTopnewsList.get(position).getTopimage(), mImageOptions);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter {

        private final ImageOptions imageOptions;

        public NewsAdapter() {
            // 设置加载中的图片(直接给ImageView用setImageResource()设置一个默认图片是不管用的), 避免图片加载过程中出现白板, 体验不好.
            imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.pic_item_list_default) // 设置加载中的图片(直接给ImageView用setImageResource()设置一个默认图片是不管用的), 避免图片加载过程中出现白板, 体验不好.
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .build();
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabBean.DataBean.NewsBean getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.item_news, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();

            TabBean.DataBean.NewsBean newsBean = mNewsList.get(position);
            x.image().bind(holder.ivIcon, newsBean.getListimage(), imageOptions);
            holder.tvTitle.setText(newsBean.getTitle());
            holder.tvDate.setText(newsBean.getPubdate());

            // 根据本地记录来标记已读未读
            String read_ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (read_ids.contains(String.valueOf(newsBean.getId()))) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {

        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
