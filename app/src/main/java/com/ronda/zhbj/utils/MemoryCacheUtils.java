package com.ronda.zhbj.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.socks.library.KLog;


/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/19
 * Version: v1.0
 * <p>
 *
 * 内存缓存的工具类
 *
 * 在过去，我们经常会使用一种非常流行的内存缓存技术的实现，即软引用或弱引用 (SoftReference or WeakReference)。但是现在已经不再推荐使用这种方式了，
 * 因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。(很容易回收掉软引用/弱引用, 则此时差不多就相当于没用使用内存缓存了)
 * 另外，Android 3.0 (API Level 11)中，图片的数据会存储在本地的内存当中，因而无法用一种可预见的方式将其释放，这就有潜在的风险造成应用程序的内存溢出并崩溃。(这种说法不太理解是什么意思)
 * Google建议使用LruCache
 */

public class MemoryCacheUtils {

    // 使用HashMap而不是ArrayList, 因为 map 是键值对形式, 便于存取
    // 当使用网络加载和本地加载时, 需要写入内存缓存
    // 注意: 这里要考虑内存溢出问题, 因为Bitmap对象占用内存比较大, 而Android系统给每个APP只分配了16M内存, 所以一旦缓存图片过多就会导致内存溢出
    //private final HashMap<String, Bitmap> mMemoryCache;
    //private final HashMap<String, SoftReference<Bitmap>> mMemoryCache; // 改造成软引用
    private final LruCache<String, Bitmap> mMemoryCache; // 二次改造, 使用LruCache, least recently use 最近最少使用


    public MemoryCacheUtils() {
        //mMemoryCache = new HashMap<>();
        long maxMemory = Runtime.getRuntime().maxMemory(); // 获取分配给app的内存大小, 单位是M, 一般是16M, 高配置手机32M
        KLog.e("maxMemory: " + maxMemory);
        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) { //设置最大缓存大小

            // 返回每个对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //int byteCount = value.getByteCount(); // 这两种写法都可以, 这不过这个方法是api12引入的
                int byteCount = value.getRowBytes() * value.getHeight();

                return byteCount / 1024; // 和构造器中的形参maxSize的单位保持一致
            }
        };
    }

    public void setMemoryCache(String url, Bitmap bitmap) {
        //mMemoryCache.put(url, bitmap);

        //mMemoryCache.put(url, new SoftReference<Bitmap>(bitmap));// 使用软引用将bitmap包装起来

        mMemoryCache.put(url, bitmap);
    }

    public Bitmap getMemoryCache(String url) {
        //Bitmap bitmap = mMemoryCache.get(url);

//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//        Bitmap bitmap = null;
//        if (softReference != null) { // 这里必须要进行判断, 因为softReference可能为null, 若再调用get()方法,则会报空指针异常
//            bitmap = softReference.get();
//        }


        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;
    }
}
