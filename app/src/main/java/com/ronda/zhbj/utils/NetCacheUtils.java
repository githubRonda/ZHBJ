package com.ronda.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/18
 * Version: v1.0
 * <p>
 * 网络缓存工具类
 */

public class NetCacheUtils {

    private final MemoryCacheUtils mMemoryCacheUtils;
    private final LocalCacheUtils mLocalCacheUtils;

    public NetCacheUtils(MemoryCacheUtils memoryCacheUtils, LocalCacheUtils localCacheUtils) {
        mMemoryCacheUtils = memoryCacheUtils;
        mLocalCacheUtils = localCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        // AsyncTask 异步封装的工具, 可以实现异步请求及主界面更新(对线程池+handler的封装)

        new BitmapTask(imageView, url).execute(); // 启动AsyncTask
    }

    /**
     * 三个泛型意义:
     * 第一个泛型:doInBackground里的参数类型, 而且也是AsyncTask.execute()中不定参数的类型, 所以execute()方法就是把形参传递给了doInBackground()
     * 第二个泛型:onProgressUpdate里的参数类型, 而且也是AsyncTask.publishProgress()中不定参数的类型,所以publishProgress()方法就是把形参传递给了onProgressUpdate(), 而且可能传递多个值,eg: 总大小, 当前进度, 百分比等, 所以是一个不定形参
     * 第三个泛型:onPostExecute里的参数类型及doInBackground的返回类型
     * <p>
     * 总结: 这三个参数就相当于事情的开始, 经过, 结果 这三个阶段.
     * 一开始, 我们需要把参数传递给子线程进行耗时操作, 操作过程中可以公布一下进度情况, 操作完成后子线程需要把结果返回并且还要结果传递给主线程中的回调方法便于更新UI
     * <p>
     * 其实把这三个参数类型都设为Void, 直接使用成员变量传递这些参数也可以.
     */
    class BitmapTask extends AsyncTask<Void, Integer, Bitmap> {

        private ImageView mImageView;
        private String mUrl;

        public BitmapTask(ImageView imageView, String url) {
            this.mImageView = imageView;
            this.mUrl = url;

            mImageView.setTag(mUrl); //setTag()方法必须要在主线程中调用
        }


        // 1.预加载, 运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 2.正在加载, 运行在子线程(核心方法), 可以直接异步请求
        @Override
        protected Bitmap doInBackground(Void... params) {

            // 开始下载图片
            Bitmap bitmap = download(mUrl);

            //publishProgress();

            return bitmap;
        }

        // 3.更新进度的方法, 运行在主线程. 调用publishProgress()会回调此方法
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        // 4.加载结束, 运行在主线程(核心方法), 可以直接更新UI
        @Override
        protected void onPostExecute(Bitmap result) {
            /**
             * 给imageView设置图片
             * 注意: 由于listview的重用机制导致imageview对象可能被多个item共用, 从而可能将错误的图片设置给了imageView对象. 每张图片都是一个异步加载并显示的过程
             * 所以需要在此处校验, 判断是否是正确的图片
             */
            if (mImageView.getTag().equals(mUrl)) {// 判断tag是否和初始设置的一样
                mImageView.setImageBitmap(result);
                KLog.d("使用网络加载图片啦");

                // 写入内存缓存
                mMemoryCacheUtils.setMemoryCache(mUrl, result);
                // 写本地缓存
                mLocalCacheUtils.setLocalCache(mUrl, result);
            }
        }
    }

    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    private Bitmap download(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);// 连接超时
            connection.setReadTimeout(5000);// 读取超时. 连接成功后,响应数据的时间

            connection.connect(); // 进行连接

            int responseCode = connection.getResponseCode();// 获取响应码
            if (responseCode == 200) { // 成功
                InputStream inputStream = connection.getInputStream();

                // 根据输入流生成bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
