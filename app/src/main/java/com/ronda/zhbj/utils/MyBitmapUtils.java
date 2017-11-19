package com.ronda.zhbj.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ronda.zhbj.R;
import com.socks.library.KLog;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/18
 * Version: v1.0
 * <p>
 * 自定义的图片加载工具类(实现三级缓存)
 */
public class MyBitmapUtils {


    private final NetCacheUtils mNetCacheUtils;
    private final LocalCacheUtils mLocalCacheUtils;
    private final MemoryCacheUtils mMemoryCacheUtils;


    public MyBitmapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mMemoryCacheUtils, mLocalCacheUtils);
    }

    public void display(ImageView imageView, String url) {

        // 设置一个默认图片
        imageView.setImageResource(R.drawable.pic_item_list_default);

        // 优先内存加载
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            KLog.d("使用内存缓存加载图片啦");
            return;
        }

        // 其次本地加载
        bitmap = mLocalCacheUtils.getLocalCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            KLog.d("使用本地缓存加载图片啦");

            // 写入内存缓存
            mMemoryCacheUtils.setMemoryCache(url, bitmap);
            return;
        }

        // 最后网络加载
        mNetCacheUtils.getBitmapFromNet(imageView, url);
    }
}
