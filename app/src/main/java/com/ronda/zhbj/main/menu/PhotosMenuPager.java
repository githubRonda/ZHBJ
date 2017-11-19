package com.ronda.zhbj.main.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ronda.zhbj.R;
import com.ronda.zhbj.bean.PhotosBean;
import com.ronda.zhbj.http.Api;
import com.ronda.zhbj.main.base.BaseMenuPager;
import com.ronda.zhbj.utils.CacheUtils;
import com.ronda.zhbj.utils.MyBitmapUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 * <p>
 * 组图详情页
 */

public class PhotosMenuPager extends BaseMenuPager implements View.OnClickListener {

    private final String mUrl;
    @ViewInject(R.id.lv_photos)
    ListView mListView;
    @ViewInject(R.id.gv_photos)
    GridView mGridView;
    private List<PhotosBean.DataBean.NewsBean> mDataList;


    public PhotosMenuPager(Activity activity, ImageButton ibPhotoType) {
        super(activity);
        mUrl = Api.PHOTOS_URL;

        ibPhotoType.setOnClickListener(this);
    }


    @Override
    public View createView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_photos, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        // 从缓存中加载
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)){
            handleData(cache);
        }

        requestDataFromService(); // 请求服务器数据
    }

    /**
     * 从服务器获取数据
     */
    private void requestDataFromService() {

        RequestParams params = new RequestParams(mUrl);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putCache(mActivity, mUrl, result); //设置缓存

                handleData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
     * 处理后台数据
     *
     * @param result
     */
    private void handleData(String result) {

        PhotosBean photosBean = new Gson().fromJson(result, PhotosBean.class);
        mDataList = photosBean.getData().getNews();

        MyAdapter myAdapter = new MyAdapter();

        mListView.setAdapter(myAdapter);
        mGridView.setAdapter(myAdapter);//GridView 和 ListView 可以共用一个Adapter
    }

    private boolean isListType = true; // 默认是列表形式的展示样式

    //标题栏右侧按钮点击切换展示样式
    @Override
    public void onClick(View v) {

        if (isListType){
            isListType = false;
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            ((ImageButton) v).setImageResource(R.drawable.icon_pic_list_type);
        }
        else{
            isListType = true;
            mListView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
            ((ImageButton) v).setImageResource(R.drawable.icon_pic_grid_type);
        }


    }


    class MyAdapter extends BaseAdapter {
        private final MyBitmapUtils myBitmapUtils;

//        private final ImageOptions mOptions;

        public MyAdapter() {
//            mOptions = new ImageOptions.Builder()
//                    .setLoadingDrawableId(R.drawable.pic_item_list_default)
//                    .setFailureDrawableId(R.mipmap.ic_launcher)
//                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                    .build();
            myBitmapUtils = new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public PhotosBean.DataBean.NewsBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_photo, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.tvTitle.setText(getItem(position).getTitle());
            //x.image().bind(holder.ivIcon, getItem(position).getListimage(), mOptions);
            myBitmapUtils.display(holder.ivIcon, getItem(position).getListimage());

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
    }
}
