package com.ronda.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 * <p>
 * 网络缓存的工具类
 * 使用 SharedPreferences 来存储
 * 其实 SharedPreferences 更适合存储一些配置信息, 因为这个本质上就是xml存储.如果存储了大量的网络数据,就会导致这个xml可读性很差, 当然也几乎没有人去看这个xml.
 * 网络缓存更通常使用的是文件缓存 或 数据库缓存
 *
 * 文件缓存: 以 url 为文件名, 以json数据为文件内容
 * 数据库缓存: 需要url和json两个字段
 *
 */

public class CacheUtils {
    /**
     * 以url为key, 以 json数据为value, 保存在本地
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putCache(Context context, String key, String value) {
        PrefUtils.putString(context, key, value);
    }

    /**
     * 获取缓存
     *
     * @param context
     * @param key
     * @return
     */
    public static String getCache(Context context, String key) {
        return PrefUtils.getString(context, key, null);
    }
}
