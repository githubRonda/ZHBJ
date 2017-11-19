# 智慧北京 #

## 项目角色 ##

- 产品经理(产品狗,产品汪)

	决定开发什么东西, 需求文档(原型图)

- 程序员(码农, 程序猿, 攻城狮)

	负责开发

	- Android开发工程师
	- IOS开发工程师
	- 服务器开发工程师JavaEE/PHP/.Net (接口文档)

- 视觉设计师/UI设计师(美工)

	效果图, 切图 1280*720分辨率(主流分辨率)

- 测试工程师

- 运营(销售, 推广, 打广告, 写软文)

## 开发流程 ##

- 需求分析

	- 聊天模块
		- 发语音
			- 取消语音
				- 上划取消
			- 语音时间限制
		- 发图片
		- 发视频
	- 朋友圈
	- 摇一摇
	- 漂流瓶

- 产品设计(需求文档)

- 需求评估(项目经理)
	
	2-3个月  300行代码/天
	确定上线时间

- 任务分配

	燃尽图

- 开始开发

- 测试 (1-2周时间测试)

- 上线(将apk发布到应用市场)

- 版本迭代(项目周期拉短 20天-30天一个迭代)

## 闪屏页面开发 ##

- 旋转动画
- 缩放动画
- 渐变动画

## 引导页
1. Activity#onCreate() 中获取 view 的宽高和margin

	    ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() { //layout方法执行结束的回调
	            // 移除监听,避免重复回调. 否则该方法一般会连续回调3次
	            ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);// 这个remove方法在API16时过时改为removeOnGlobalLayoutListener. 估计是google工程师命名时马虎了
	
	            // 计算两个圆点的距离
	            // 移动距离=第二个圆点left值 - 第一个圆点left值
	            mPointDistance = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
	        }
	    });

2. 引导页中红色的小圆点的移动实现: 给 ViewPager 设置监听器, 当滑动时计算红色小圆点应该移动的偏移量, 然后更新红色小圆点的LayoutParams的leftMargin参数.  要注意:onPageScrolled()方法的第一个参数position的值比较特别

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 滚动时调用
             * @param position   向右滑时，表示当前page的索引，翻页成功时position才会+1； 向左滑时, 表示当前page的索引-1，翻页不成功时position又会变成当前page的索引值。但是position是在[0, count-1]这个范围的
             * @param positionOffset 当前偏移量的比例。范围[0, 1). 从左往右是从0逐渐变为1，从右往左是从1逐渐变为0
             * @param positionOffsetPixels 当前偏移量的具体值。单位px
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //KLog.i("onPageScrolled --> position: " + position + ", positionOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);

                int offset = (int) (position * mPointDistance + mPointDistance * positionOffset);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                layoutParams.leftMargin = offset;
                //这里 setLayoutParams 和 requestLayout 是可以只用写其中一个的, 因为layoutParams本身就是一个引用, 而且 setLayoutParam()内部也调用了 requestLayout() 方法
                ivRedPoint.setLayoutParams(layoutParams);
                //ivRedPoint.requestLayout();
            }
			....
		}

## 库项目 Library ##
引入库项目后, 在android项目中,既可以访问Library项目中的java文件, 也可以访问Library项目中所有资源(res)文件. 而jar包中只能访问java文件, 功能比jar包更强大!!!

## 侧边栏 ##

> SlidingMenu 引入有三种方式

  * 让Activity 继承自 SlidingActivity
	  * setBehindContentView() 设置侧边栏布局
  * SlidingMenu menu = new SlidingMenu(this); menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
	  * menu.setMenu(R.layout.menu); 设置侧边栏布局
  * 在布局文件中使用 SlidingMenu 控件

https://github.com/jfeinstein10/SlidingMenu

基本用法:

    mSlidingMenu = new SlidingMenu(this);
    mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

    //设置侧边栏从左边拉出
    mSlidingMenu.setMode(SlidingMenu.LEFT);
    //设置全屏触摸范围可拉出侧边栏
    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    //设置内容区的宽度,而不是侧边栏的宽度
    mSlidingMenu.setBehindOffset(500);

    //设置侧边栏的布局文件
    mSlidingMenu.setMenu(R.layout.left_menu);
    //menu.setSecondaryMenu(R.layout.right_menu);//设置第二个侧边栏(一般是右边)


    /**
     * 是否启用侧边栏
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        if (enable){
            mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


## UI框架分析

> 整个主界面分为:首页, 新闻中心, 智慧服务, 政务, 设置 共5个分类
 
> 新闻中心中分为: 新闻, 专题, 组图, 互动  共4个分类

>


* 整个主界面的布局: ViewPager + RadioGroup 的上下结构的布局
* 每个Pager中: title + FrameLayout 的上下结构的布局, 并且向上抽取, 把这个Pager对应的布局装载到了BasePager这个基类中(提供两个方法: initView() initData()), 然后派生5个对应分类的子类. 当Page被选中的时候调用对应initData()向FrameLayout中添加内容
* 在新闻中心这个Page中


## ViewPager的滑动禁用

    // 原生的ViewPager之所以可以滑动,肯定是在onTouchEvent()中进行了处理. 所以只要复写了这个方法并且直接返回true, 则ViewPager就滑动不了了
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 重写此方法, 触摸时什么都不做, 从而实现对滑动事件的禁用
        return true;
    }

## xUtils3

xUtils3 是 xUtils 的升级版

https://github.com/wyouflf/xUtils

https://github.com/wyouflf/xUtils3

简介

* xUtils 包含了orm, http(s), image, view注解, 但依然很轻量级(246K), 并且特性强大, 方便扩展:
  - `稳定的基石`: `AbsTask`和统一的回调接口`Callback`, 任何异常, 即使你的回调方法实现有异常都会进入`onError`, 任何情况下`onFinished`总会让你知道任务结束了.
  - 基于高效稳定的`orm`工具, `http`模块得以更方便的实现cookie(支持domain, path, expiry等特性)和
    缓存(支持Cache-Control, Last-Modified, ETag等特性)的支持.
  - 有了强大的`http`及其下载缓存的支持, `image`模块的实现相当的简洁, 并且支持回收被view持有, 但被Mem Cache移除的图片, 减少页面回退时的闪烁..
  - `view`注解模块仅仅400多行代码却灵活的支持了各种View注入和事件绑定, 包括拥有多了方法的listener的支持.



 其他特性

* 支持超大文件(超过2G)上传
* 更全面的http请求协议支持(11种谓词)
* 拥有更加灵活的ORM, 和greenDao一致的性能
* 更多的事件注解支持且不受混淆影响...
* 图片绑定支持gif(受系统兼容性影响, 部分gif文件只能静态显示), webp; 支持圆角, 圆形, 方形等裁剪, 支持自动旋转...
* 从3.5.0开始不再包含libwebpbackport.so, 需要在Android4.2以下设备兼容webp的请使用3.4.0版本.



还是四大模块:orm(db), http(s), image, view

添加依赖:

	compile 'org.xutils:xutils:3.5.0'

混淆配置:
	
	################### region for xUtils
	-keepattributes Signature,*Annotation*
	-keep public class org.xutils.** {
	    public protected *;
	}
	-keep public interface org.xutils.** {
	    public protected *;
	}
	-keepclassmembers class * extends org.xutils.** {
	    public protected *;
	}
	-keepclassmembers @org.xutils.db.annotation.* class * {*;}
	-keepclassmembers @org.xutils.http.annotation.* class * {*;}
	-keepclassmembers class * {
	    @org.xutils.view.annotation.Event <methods>;
	}
	#################### end region

常见问题:

1. 更好的管理图片缓存: https://github.com/wyouflf/xUtils3/issues/149
2. Cookie的使用: https://github.com/wyouflf/xUtils3/issues/125
3. 关于query参数? http请求可以通过 header, url, body(请求体)传参; query参数是url中问号(?)后面的参数.
4. 关于body参数? body参数只有PUT, POST, PATCH, DELETE(老版本RFC2616文档没有明确指出它是否支持, 所以暂时支持)请求支持.
5. 自定义Http参数对象和结果解析: https://github.com/wyouflf/xUtils3/issues/191

权限配置:

	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

初始化

	// 在application的onCreate中初始化
	@Override
	public void onCreate() {
	    super.onCreate();
	    x.Ext.init(this);
	    x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
	    ...
	}

> View模块

	// xUtils的view注解要求必须提供id，以使代码混淆不受影响。
	@ViewInject(R.id.textView)
	TextView textView;
	
	
	@ResInject(id = R.string.label, type = ResType.String)
	private String label;
	
	/**
	 * 1. 方法必须私有限定,
	 * 2. 方法参数形式必须和type对应的Listener接口一致.
	 * 3. 注解参数value支持数组: value={id1, id2, id3}
	 * 4. 其它参数说明见{@link org.xutils.event.annotation.Event}类的说明.
	 **/
	@Event(value = R.id.btn_test_baidu1,
	        type = View.OnClickListener.class/*可选参数, 默认是View.OnClickListener.class*/)
	private void onTestBaidu1Click(View view) {
	...
	}
	
	//在Activity中注入
	...
	@ContentView(R.layout.activity_main)
	public class MainActivity extends Activity {
		//在Activity中注入：
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			//ViewUtils.inject(this); //注入view和事件
			 x.view().inject(this);
			...
			textView.setText("some text...");
			...
		}
	}
	//在Fragment中注入：
	@ContentView(R.layout.fragment_image)
	public class ImageFragment extends Fragment {
		private boolean injected = false;
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			injected = true;
			return x.view().inject(this, inflater, container);
		}
	
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			if (!injected) {
				x.view().inject(this, this.getView());
			}
		}
	}

	
	x.view().inject((Activity activity); // 注入activity
	x.view().inject(Object fragment, LayoutInflater inflater, ViewGroup container); //注入fragment
	x.view().inject(Object handler, View view); //注入view holder --> 一个普通的java类中加载一个布局时使用
	x.view().inject(View view); // 注入view --> 这个不知道什么时候使用


> http模块

	//=========================简单的版本========================================
	@Event(value = R.id.btn_test_baidu2)
	private void onTestBaidu2Click(View view) {
	    RequestParams params = new RequestParams("https://www.baidu.com/s");
	    params.setSslSocketFactory(...); // 设置ssl
	    params.addQueryStringParameter("wd", "xUtils");
	    x.http().get(params, new Callback.CommonCallback<String>() {
	        @Override
	        public void onSuccess(String result) {
	            Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
	        }
	
	        @Override
	        public void onError(Throwable ex, boolean isOnCallback) {
	            Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
	        }
	
	        @Override
	        public void onCancelled(CancelledException cex) {
	            Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
	        }
	
	        @Override
	        public void onFinished() {
	
	        }
	    });
	}


	//=========================完整版本========================================
	/**
	 * 自定义实体参数类请参考:
	 * 请求注解 {@link org.xutils.http.annotation.HttpRequest}
	 * 请求注解处理模板接口 {@link org.xutils.http.app.ParamsBuilder}
	 *
	 * 需要自定义类型作为callback的泛型时, 参考:
	 * 响应注解 {@link org.xutils.http.annotation.HttpResponse}
	 * 响应注解处理模板接口 {@link org.xutils.http.app.ResponseParser}
	 *
	 * 示例: 查看 org.xutils.sample.http 包里的代码
	 */
	BaiduParams params = new BaiduParams();
	params.wd = "xUtils";
	// 有上传文件时使用multipart表单, 否则上传原始文件流.
	// params.setMultipart(true);
	// 上传文件方式 1
	// params.uploadFile = new File("/sdcard/test.txt");
	// 上传文件方式 2
	// params.addBodyParameter("uploadFile", new File("/sdcard/test.txt"));
	Callback.Cancelable cancelable
	       = x.http().get(params,
	       /**
	        * 1. callback的泛型:
	        * callback参数默认支持的泛型类型参见{@link org.xutils.http.loader.LoaderFactory},
	        * 例如: 指定泛型为File则可实现文件下载, 使用params.setSaveFilePath(path)指定文件保存的全路径.
	        * 默认支持断点续传(采用了文件锁和尾端校验续传文件的一致性).
	        * 其他常用类型可以自己在LoaderFactory中注册,
	        * 也可以使用{@link org.xutils.http.annotation.HttpResponse}
	        * 将注解HttpResponse加到自定义返回值类型上, 实现自定义ResponseParser接口来统一转换.
	        * 如果返回值是json形式, 那么利用第三方的json工具将十分容易定义自己的ResponseParser.
	        * 如示例代码{@link org.xutils.sample.http.BaiduResponse}, 可直接使用BaiduResponse作为
	        * callback的泛型.
	        *
	        * 2. callback的组合:
	        * 可以用基类或接口组合个种类的Callback, 见{@link org.xutils.common.Callback}.
	        * 例如:
	        * a. 组合使用CacheCallback将使请求检测缓存或将结果存入缓存(仅GET请求生效).
	        * b. 组合使用PrepareCallback的prepare方法将为callback提供一次后台执行耗时任务的机会,
	        * 然后将结果给onCache或onSuccess.
	        * c. 组合使用ProgressCallback将提供进度回调.
	        * ...(可参考{@link org.xutils.image.ImageLoader}
	        * 或 示例代码中的 {@link org.xutils.sample.download.DownloadCallback})
	        *
	        * 3. 请求过程拦截或记录日志: 参考 {@link org.xutils.http.app.RequestTracker}
	        *
	        * 4. 请求Header获取: 参考 {@link org.xutils.http.app.RequestInterceptListener}
	        *
	        * 5. 其他(线程池, 超时, 重定向, 重试, 代理等): 参考 {@link org.xutils.http.RequestParams}
	        *
	        **/
	       new Callback.CommonCallback<String>() {
	           @Override
	           public void onSuccess(String result) {
	               Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
	           }
	
	           @Override
	           public void onError(Throwable ex, boolean isOnCallback) {
	               //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
	               if (ex instanceof HttpException) { // 网络错误
	                   HttpException httpEx = (HttpException) ex;
	                   int responseCode = httpEx.getCode();
	                   String responseMsg = httpEx.getMessage();
	                   String errorResult = httpEx.getResult();
	                   // ...
	               } else { // 其他错误
	                   // ...
	               }
	               Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
	           }
	
	           @Override
	           public void onCancelled(CancelledException cex) {
	               Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
	           }
	
	           @Override
	           public void onFinished() {
	
	           }
	       });
	
	// cancelable.cancel(); // 取消请求


	//=========================带有缓存的请求示例========================================
	BaiduParams params = new BaiduParams();
	params.wd = "xUtils";
	// 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
	params.setCacheMaxAge(1000 * 60);
	Callback.Cancelable cancelable
	    	// 使用CacheCallback, xUtils将为该请求缓存数据.
			= x.http().get(params, new Callback.CacheCallback<String>() {
	
		private boolean hasError = false;
		private String result = null;
	
		@Override
		public boolean onCache(String result) {
			// 得到缓存数据, 缓存过期后不会进入这个方法.
			// 如果服务端没有返回过期时间, 参考params.setCacheMaxAge(maxAge)方法.
	        //
	        // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
	        //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
	        //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
	        //
	        // * 如果信任该缓存返回 true, 将不再请求网络;
	        //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
	        //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
	        //
	        this.result = result;
	        return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
		}
	
		@Override
		public void onSuccess(String result) {
			// 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
	        if (result != null) {
			    this.result = result;
			}
		}
	
		@Override
		public void onError(Throwable ex, boolean isOnCallback) {
			hasError = true;
			Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
			if (ex instanceof HttpException) { // 网络错误
				HttpException httpEx = (HttpException) ex;
				int responseCode = httpEx.getCode();
				String responseMsg = httpEx.getMessage();
				String errorResult = httpEx.getResult();
				// ...
			} else { // 其他错误
				// ...
			}
		}
	
		@Override
		public void onCancelled(CancelledException cex) {
			Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onFinished() {
			if (!hasError && result != null) {
				// 成功获取数据
				Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
			}
		}
	});

> image 模块

	图片的缓存路径:/storage/sdcard/Android/data/包名/cache/xUtils_img/ 目录下

	//基本形式
	x.image().bind(ImageView view, String url);
	x.image().bind(ImageView view, String url, ImageOptions options);
	x.image().bind(ImageView view, String url, Callback.CommonCallback<Drawable> callback);
	x.image().bind(ImageView view, String url, ImageOptions options, Callback.CommonCallback<Drawable> callback);

    Callback.Cancelable loadDrawable(String url, ImageOptions options, Callback.CommonCallback<Drawable> callback);
    Callback.Cancelable loadFile(String url, ImageOptions options, Callback.CacheCallback<File> callback);

	x.image().clearMemCache();
    x.image().clearCacheFiles();

	
	//实际使用
	// assets file
	x.image().bind(imageView, "assets://test.gif", imageOptions);
	
	// local file
	x.image().bind(imageView, new File("/sdcard/test.gif").toURI().toString(), imageOptions);
	x.image().bind(imageView, "/sdcard/test.gif", imageOptions);
	x.image().bind(imageView, "file:///sdcard/test.gif", imageOptions);
	x.image().bind(imageView, "file:/sdcard/test.gif", imageOptions);

	x.image().bind(imageView, url, imageOptions, new Callback.CommonCallback<Drawable>() {...});
	x.image().loadDrawable(url, imageOptions, new Callback.CommonCallback<Drawable>() {...});
	// 用来获取缓存文件
	x.image().loadFile(url, imageOptions, new Callback.CommonCallback<File>() {...});


	/*
	 * 一个ListView列表加载图片的例子
	 */
	imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();

	private class ImageListAdapter extends BaseAdapter {
		...

	    @Override
	    public View getView(final int position, View view, ViewGroup parent) {
	        ImageItemHolder holder = null;
	        if (view == null) {
	            view = mInflater.inflate(R.layout.image_item, parent, false);
	            holder = new ImageItemHolder();
	            x.view().inject(holder, view);
	            view.setTag(holder);
	        } else {
	            holder = (ImageItemHolder) view.getTag();
	        }
	        holder.imgPb.setProgress(0);
	        x.image().bind(holder.imgItem,
	                imgSrcList.get(position),
	                imageOptions,
	                new CustomBitmapLoadCallBack(holder));
	        return view;
	    }
	}

    private class ImageItemHolder {
        @ViewInject(R.id.img_item)
        private ImageView imgItem;

        @ViewInject(R.id.img_pb)
        private ProgressBar imgPb;
    }

	public class CustomBitmapLoadCallBack implements Callback.ProgressCallback<Drawable> {
        private final ImageItemHolder holder;

        public CustomBitmapLoadCallBack(ImageItemHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onWaiting() {
            this.holder.imgPb.setProgress(0);
        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            this.holder.imgPb.setProgress((int) (current * 100 / total));
        }

        @Override
        public void onSuccess(Drawable result) {
            this.holder.imgPb.setProgress(100);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }

	


	


> ORM模块

	Parent test = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
	long count = db.selector(Parent.class).where("name", "LIKE", "w%").and("age", ">", 32).count();
	List<Parent> testList = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findAll();


## 网络缓存(3种缓存形式 和 2 种处理方式)
* SharedPreferences 缓存: 以url 或 md5(url)为key, 以 json数据为value
* 文件缓存: 以 url 为文件名, 以json数据为文件内容
* 数据库缓存: 需要url和json两个字段

	其实 SharedPreferences 更适合存储一些配置信息, 因为这个本质上就是xml存储.如果存储了大量的网络数据,就会导致这个xml可读性很差, 当然也几乎没有人去看这个xml.

> 缓存的处理方式:(一般有2种方式)
>
 * 方式一: 给缓存设置有效期. (目的是节省用户流量,并且保证数据的更新)
 * 方式二: 读缓存的同时,继续访问服务器. (咋一看设置的缓存是没有意义的. 但其实此时缓存的作用就是:快速加载数据, 让用户可以快速的先看到一些东西, 等服务器数据返回后再继续更新数据) (目的是:快速加载数据,提高流畅度)


## ListView 

> setOnItemClickListener() 和 setOnItemSelectedListener() 区别:

> 相同点: 都定义在AdapterView中,

> 不同点: 
AbsListView 一般使用 setOnItemClickListener(). eg: ListView.setOnItemClickListener() : 表示点击了某一项
AbsSpinner 一般使用 setOnItemSelectedListener(). eg: Spinner.setOnItemSelectedListener() : 表示选中了某一项

* ListView 中如何高亮显示点击(选中)了某一项
	* 方法一: 定义 mCurPosition 变量, 在Adapter#getView()中, 使用java代码设置改变背景或颜色
	* 方法二: xml中使用Selector选择器,设置给ItemView. java中定义 mCurPosition 变量, 在Adapter#getView()中, 设置itemView.是否可用(Enabled)

  对于方法二, 要说明, 即使把ListView中的item的根布局设置为setEnabaled(false) 不可用状态, 但是该item还是可以相应OnItemClickListener事件的. 若是直接给item设置的OnClick事件是不会相应的.


## ViewPagerIndicator 的用法
https://github.com/JakeWharton/ViewPagerIndicator


ViewPagerIndicator使用流程:
 
 * 1.引入库 
 * 2.解决support-v4冲突(让两个版本一致) 
 * 3.参考例子中的布局文件和java代码

		ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);

	    class GoogleMusicAdapter extends FragmentPagerAdapter {
	       
			//指示器的标题
	        @Override
	        public CharSequence getPageTitle(int position{
	            return CONTENT[position % CONTENT.length].toUpperCase();
	        }
	
	  		...
	    }


 * 4.给Activity设置ViewPagerIndicator库中的主题 并且还可以在库中对该主题进行调整(例如:更改选中的背景, 字体颜色等)


产生的问题:

> 1. 在向右快速滑动ViewPagerIndicator的过程中,有时会把侧边栏给拉出来.
	> 原因分析: SlidingMenu 有时会拦截 ViewPagerIndicator 的事件. SlidingMenu是附件在Activity上的.
	> 
	> 解决方法: 在ViewPagerIndicator中找到 TabPageIndicator源码, 复写dispatchTouchEvent()方法,请求父控件不要拦截touch事件
		
    // 事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 请求父级节点和祖先节点不要拦截touch事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

> 2. 每次向右滑动 ViewPager 的时候,也会把侧边栏给拉出来.
	> 原因分析: SlidingMenu 有时会拦截 ViewPager 的事件. SlidingMenu是附件在Activity上的.
	> 
	> 解决方法: 这个解决方法和ViewPagerIndicator的不同, 因为当滑动ViewPager的时候,并不是完全禁止SlidingMenu的拉出. 当处于第一个tab时, 向右滑动ViewPager,要拉出侧边栏. 所以解决方法就是给ViewPager设置监听, 根据当前页面的位置设置SlidingMenu是否禁止滑动

    //给ViewPager设置监听有两种方法:1. ViewPager.addOnPageChangeListener() 2. TabPageIndicator.setOnPageChangeListener()
	// 千万不能使用ViewPager.setOnPageChangeListener(), 因为这个会和 TabPageIndicator 有冲突,导致Indicator的选中效果失效
    mViewPager.addOnPageChangeListener(this);
    或者
    mIndicator.setOnPageChangeListener(this);
	//mViewPager.setOnPageChangeListener(this); // 不能使用这种方式
	总体上还是建议给Indicator设置事件监听, 这样方便记忆,而且不容易出现bug


## Tab中顶部的TopView对应的ViewPager滑动事件冲突的处理

	/**
	 * Tab 中的顶部的 TopNews 对应的 ViewPager
	 * 之所以要复写这个ViewPager是因为这个TopNews对应的 ViewPager 和 Tab内容本身所在的ViewPager的滑动事件是有冲突的
	 * 需求: 向左滑动TopNewsViewPager,当滑动到最后一个时,再次左滑, 则Tab跳转到下一个. 同理, 向右滑动时,当滑动到第一个时,再次右滑, 则Tab跳转到上一个.
	 */
	
	public class TopNewsViewPager extends ViewPager {
	    private int startY;
	    private int startX;
	
	    public TopNewsViewPager(Context context) {
	        super(context);
	    }
	
	    public TopNewsViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	
	    /**
	     * 0. 先统一请求父级不要拦截事件. 具体的情况后面在判断
	     * 1. 上下滑动,不需要请求父控件不要拦截事件, 因为当前ViewPager不需要处理该事件
	     * 2. 向右滑动并且当前是第一个页面,不需要请求父控件不要拦截事件, 因为当前ViewPager不需要处理该事件, 可由Tab对应的ViewPager来处理,从而滑动tab
	     * 3. 向左滑动并且当前是最后一个页面,不需要请求父控件不要拦截事件
	     */
	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {
	        getParent().requestDisallowInterceptTouchEvent(true);// 先统一请求父级不要拦截事件. 具体的情况后面在判断
	
	        switch (ev.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                startX = (int) ev.getX();
	                startY = (int) ev.getY();
	
	                break;
	            case MotionEvent.ACTION_MOVE:
	
	                int dx = (int) (ev.getX() - startX);
	                int dy = (int) (ev.getY() - startY);
	
	                if (Math.abs(dx) > Math.abs(dy)) {
	
	                    if (dx < 0) {
	                        //向左滑
	                        int count = getAdapter().getCount();
	                        if (getCurrentItem() == count - 1) {
	                            getParent().requestDisallowInterceptTouchEvent(false);
	                        }
	                    } else {
	                        //向右滑
	                        if (getCurrentItem() == 0) {
	                            getParent().requestDisallowInterceptTouchEvent(false);
	                        }
	                    }
	                } else {
	                    //上下滑动. (当前ViewPager不需要处理事件)
	                    getParent().requestDisallowInterceptTouchEvent(false);
	                }
	
	                break;
	            case MotionEvent.ACTION_UP:
	
	                break;
	        }
	        return super.dispatchTouchEvent(ev);
	    }
	}

## Tab中顶部的TopView对应的ViewPager中的指示器
和tab一样,仍然是使用的是ViewPagerIndicator这个开源框架, 只不过使用的是CirclePageIndicator而不是TabPageIndicator.

这里注意会有一个小bug: 切换tab时,再回到之前的tab时, 发现页面是销毁后重新初始化的,但是indicator仍然是保留上次圆点的位置的bug

解决方法:

	// 每次页面重新创建时, 默认让第一个选中
	// 注意:只能使用mIndicator.onPageSelected(0); 不能使用mIndicator.setCurrentItem(0); 因为后面这个方法是不能改变小圆点选中的位置的!!!
	mIndicator.onPageSelected(0);


自动轮播:

 	if (mHandler==null){
        // 保证启动自动轮播逻辑只执行一次
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                int currentItem = mViewPager.getCurrentItem();
                currentItem = (currentItem + 1)% mTopnewsList.size();
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



Activity 永远都是填充屏幕的, 不可能像View一样, 仅仅占用屏幕中的某一区域. 即使是窗口模式的Activity(对话框主题的Activity), 也是填充整个屏幕区域的, 只不过是内边距设置成了半透明色, 看起来像是一个处于中间区域的对话框


如何知道一个app是否是网页版开发的,还是使用原生android开发的呢?
设置 --> 开发者选项 --> 显示布局边界 ==> 这样就可以到每个空间的边界线, 若是网页开发的, 就会显示出一个大的WebView的边界, 而且里面的复杂布局不会显示出任何边界. 很容易区分...



##WebView的使用
	mWebView.loadUrl("http://www.baidu.com");

    WebSettings settings = mWebView.getSettings();
    //网页分两种:一种是针对电脑浏览器的; 另一种是wap网页,针对手机上网页的, 对于WAP网页,即使设置了显示缩放按钮, 支持双击缩放也不会有效果.因为这个网页已经对手机进行了适配,无需再次进行缩放
    settings.setBuiltInZoomControls(true); // 显示缩放按钮(wap网页不支持)
    settings.setUseWideViewPort(true); //支持双击缩放(wap网页不支持)
    settings.setJavaScriptEnabled(true); // 支持js功能, 默认禁掉js
	settings.setTextSize(WebSettings.TextSize.LARGEST); // 参数是一个枚举, 共有5个值.(已过时)
    //settings.setTextZoom(1); // API14添加的方法. 扩展性更强.因为参数是一个int类型的值, 可以随便设置

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


## ShareSDK 
http://www.mob.com/

集成文档:http://wiki.mob.com/sdk-share-android-3-0-0/

其实ShareSDK就是一个大管家, 他是把所有的开放平台(QQ, 微信, 微博, 支付宝等所有平台)的sdk都集成到了一起, 便于开发者使用!
但实际上, 开发者在使用的过程中, 还是需要在对应的各个开放平台上去创建应用,申请APPID, 然后在项目中 assets/ShareSDK.xml 和AndroidManifest.xml 中进行配置, 这样我们分享出去的东西才是显示来自当前的应用,否则就是显示来自"ShareSDK"字样

在微博, QQ空间 等很多平台的控制台创建应用时,需要审核, 一般需要3天左右


## 图片缓存

> 三级缓存:
 
- 优先从内存中加载图片, 速度最快, 不消耗流量
- 其实是本地(sdcard)加载图片, 速度较快, 不消耗流量 
- 最后是网络请求下载图片, 速度最慢, 消耗流量

> 内存溢出

- 不管android设备总内存是多大, 都只给每个app分配固定的内存大小, 16M, 所以一旦超出这个值,就会内存溢出!

> 垃圾回收器特点:

- 回收没有引用的堆内存对象
- 回收内存有一定的滞后性. 并不是对象没有引用后就立即回收

> 引用(Reference)

- 强引用, 默认形式, 垃圾回收期不会回收
- 软引用, 垃圾回收器会考虑回收 (SoftReference)
- 弱引用, 垃圾回收器更会考虑回收 (WeakReference)
- 虚引用, 垃圾回收器最优先考虑回收 (PhantomReference)



## 屏幕适配 

> 养成良好的开发习惯: 多用dp,sp,不用px; 多用线性布局和相对布局, 不用绝对布局; 代码中如果必须设置像素的话, 将dp转为px进行设置

> 项目开发后期,对适配问题进行验证

- 图片适配
	
	ldpi:240*320  0.75
	mdpi: 320*240  1
	hdpi: 480*800  1.5
	xhdpi: 1280*720 2
	xxhdpi: 1920*1080 3

	设备密度: 

	常规做法: 做一套图 1280*720 切图, 放在hdpi或xhdpi下. 如果某个屏幕出了问题, 再针对该屏幕, 对相关出问题的图片进行替换.

- 布局适配(不太常用)

	layout-800x480:专门针对480*800屏幕适配的布局文件, 一般只调整位置和大小, 不建议对控件类型和个数进行调整

- 尺寸适配(很常用)
	
	//dp 和 px
	//dp = px/设备密度
	//values-1280x720/dimens.xml
	
- 权重适配

	 android:weightSum="3"

- 代码适配
