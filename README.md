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

还是四大模块:orm(db), http(s), image, view

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


> http模块

	//简单的版本
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