package com.ronda.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.xutils.common.util.MD5;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/18
 * Version: v1.0
 * <p>
 * 本地缓存工具类
 */

public class LocalCacheUtils {
    private static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory() + "/zhbj_cache";


    // 写本地缓存
    public void setLocalCache(String url, Bitmap bitmap) {

        File dir = new File(LOCAL_CACHE_PATH);
        // 创建文件夹
        if (!dir.exists() || !dir.isDirectory()) {//如果文件夹不存在, 或者不是一个目录
            dir.mkdirs(); //创建多级目录
        }

        try {
            //因为文件的命名对于url中的某些字符有限制, 所以采用md5对其进行加密. 只有文件有这种要求
            String fileName = MD5Encoder.encode(url);

            //把图片压缩,然后写入到一个输出流中. 保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(dir, fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读本地缓存
    public Bitmap getLocalCache(String url) {

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(LOCAL_CACHE_PATH + File.separator + MD5Encoder.encode(url));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
