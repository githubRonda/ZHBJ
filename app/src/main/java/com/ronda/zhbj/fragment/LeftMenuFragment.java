package com.ronda.zhbj.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ronda.zhbj.MainActivity;
import com.ronda.zhbj.R;
import com.ronda.zhbj.bean.NewsMenu;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/13
 * Version: v1.0
 * <p>
 * 侧边栏fragment
 */

@ContentView(R.layout.fragment_left_menu)
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_menu)
    ListView mListView;

    private List<NewsMenu.DataBean> mDataList;

    private int mCurPosition = 0;
    private MyAdapter mAdapter;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void initData() {
    }

    /**
     * 设置侧边栏数据
     *
     * @param data
     */
    public void setMenuData(List<NewsMenu.DataBean> data) {
        mCurPosition = 0; // 归零

        mDataList = data;

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurPosition = position;
                mAdapter.notifyDataSetChanged();

                //收起侧边栏(此时侧边栏肯定是打开的,所以toggleSlidingMenu()肯定是关闭侧边栏)
                ((MainActivity) mActivity).toggleSlidingMenu();

                // 侧边栏点击之后, 要通知新闻中心切换其FrameLayout中的内容
                ((MainActivity) mActivity).notifyLeftMenuClicked(position);
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public NewsMenu.DataBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 总共就4个item, 这里就没必要使用ViewHolder了
            View view = View.inflate(mActivity, R.layout.item_left_menu, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            textView.setText(getItem(position).getTitle());

            if (mCurPosition == position){
                textView.setEnabled(true);
            }
            else{
                textView.setEnabled(false);
            }


            return view;
        }
    }
}
