package com.sigmatrix.yhmh5.yhmh5.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.smtt.sdk.QbSdk;

import org.xutils.x;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
	/**
	 * Activity列表 
	 */
	private List<Activity> activityList = new LinkedList<Activity>();
	private static String androidId;
	private static Context mContext;
	/**
	 *全局唯一实例
	 */
	private static MyApplication instance;
	/**
	 * 获取类实例对象
	 * 
	 * @return MyActivityManager
	 */
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}
	/**
	 * 保存Activity到现有列表中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	/**
	 * 关闭保存的Activity
	 */
	public void exit() {
		if (activityList != null) {
			Activity activity;
			for (int i = 0; i < activityList.size(); i++) {
				activity = activityList.get(i);
				if (activity != null) {
					if (!activity.isFinishing()) {
						activity.finish();
					}

					activity = null;
				}
				activityList.remove(i);
				i--;
			}
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		getAndroidId();
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.e("加载", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		x.Ext.init(this);
		initImageLoader(getApplicationContext());
	}

	public static Context getContext() {
		return mContext;
	}

	public static String getAndroidId() {
		if (TextUtils.isEmpty(androidId)) {
			androidId = Secure.getString(mContext.getContentResolver(),
					Secure.ANDROID_ID);
		}
		return androidId;
	}
	

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "qsmx"
				+ File.separatorChar + "Little" + File.separatorChar + "cache");
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				// .displayer(new FadeInBitmapDisplayer(500))
				.imageScaleType(ImageScaleType.EXACTLY).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.defaultDisplayImageOptions(options).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
