package com.ronda.zhbj;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.socks.library.KLog;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/13
 * Version: v1.0
 * <p>
 * 新闻详情页面
 */
public class NewsDetailActivity extends Activity {

    @ViewInject(R.id.ib_back)
    ImageButton mIbBack;
    @ViewInject(R.id.ib_menu)
    ImageButton mIbMenu;
    @ViewInject(R.id.ll_control)
    LinearLayout mLlControl;
    @ViewInject(R.id.ib_share)
    ImageButton mIbShare;
    @ViewInject(R.id.ib_textsize)
    ImageButton mIbTextSize;
    @ViewInject(R.id.pb_loading)
    ProgressBar mPbLoading;
    @ViewInject(R.id.web_view)
    WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setContentView(R.layout.activity_news_detail);
        x.view().inject(this);

        initTitle();


        //mWebView.loadUrl("http://www.baidu.com");
        mUrl = getIntent().getStringExtra("url");
        if (mUrl != null) {
            mWebView.loadUrl(mUrl);
        }

        WebSettings settings = mWebView.getSettings();
        //网页分两种:一种是针对电脑浏览器的; 另一种是wap网页,针对手机上网页的, 对于WAP网页,即使设置了显示缩放按钮, 支持双击缩放也不会有效果.因为这个网页已经对手机进行了适配,无需再次进行缩放
        settings.setBuiltInZoomControls(true); // 显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true); //支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true); // 支持js功能, 默认禁掉js

        mWebView.setWebViewClient(new WebViewClient() {
            //开始加载页面
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                KLog.d("开始加载页面");
                mPbLoading.setVisibility(View.VISIBLE);
            }

            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                KLog.d("页面加载完成");
                mPbLoading.setVisibility(View.INVISIBLE);
            }

            //所有连接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 在跳转链接时强制在当前webview中加载, 不用跳到浏览器app中
                return true;
                //return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // 网页加载进度发生变化时调用
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                KLog.d("进度: " + newProgress);
            }

            //收到网页标题时调用
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //收到网页图标时调用
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

        // mWebView.goBack();//跳到上个页面
        // mWebView.goForward();//跳到下个页面

    }

    private void initTitle() {
        mIbMenu.setVisibility(View.GONE);
        mIbBack.setVisibility(View.VISIBLE);
        mLlControl.setVisibility(View.VISIBLE);
    }

    @Event({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:

                showTextSizeDialog();
                break;
            case R.id.ib_share:
                showShare();
                break;
        }
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTheme(OnekeyShareTheme.CLASSIC); // 传统主题, 默认值

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }


    /**
     * 显示修改字体大小的对话框
     */
    private int mCurTextSizeWhich = 2; // 记录当前字体. 默认是正常字体, 索引值为2
    private int mTextSizeChoiceWhich; // 记录选中的字体
    private void showTextSizeDialog() {

        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        new AlertDialog.Builder(this)
                .setTitle("字体设置")
                .setSingleChoiceItems(items, mCurTextSizeWhich, new DialogInterface.OnClickListener() { // 默认选中正常字体, 索引值为2
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextSizeChoiceWhich = which;
                    }
                })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebSettings settings = mWebView.getSettings();

                        mCurTextSizeWhich = mTextSizeChoiceWhich;
                        switch (mTextSizeChoiceWhich){
                            case 0: //超大号字体
                                //设置字体大小有两种方式
                                settings.setTextSize(WebSettings.TextSize.LARGEST); // 参数是一个枚举, 共有5个值.(已过时)
                                //settings.setTextZoom(1); // API14添加的方法. 扩展性更强.因为参数是一个int类型的值, 可以随便设置
                                break;
                            case 1: //大号字体
                                settings.setTextSize(WebSettings.TextSize.LARGER);
                                break;
                            case 2: //正常字体
                                settings.setTextSize(WebSettings.TextSize.NORMAL);
                                break;
                            case 3: //小号字体
                                settings.setTextSize(WebSettings.TextSize.SMALLER);
                                break;
                            case 4://超小号字体
                                settings.setTextSize(WebSettings.TextSize.SMALLEST);
                                break;
                        }

                    }
                }).show();
    }
}
