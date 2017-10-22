package com.ronda.zhbj.http;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/15
 * Version: v1.0
 */

public class Api {


    private static final String SERVER_URL = "http://10.0.3.2:8080/zhbj"; // Genymotion要使用10.0.3.2,而不是10.0.2.2

    /**
     * 分类信息
     */
    public static final String CATEGORY_URL = SERVER_URL + "/categories.json";
}
