package com.jieshoufuwu.assist;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.jieshoufuwu.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MyApplication extends Application{
	private static Context context;
	public static DisplayImageOptions options;

	public void onCreate(){
		super.onCreate();
		MyApplication.context=getApplicationContext();
		initImageLoader();
		initServiceIp();
	}

	public static Context getAppContext(){
		return MyApplication.context;
	}

	private void initServiceIp() {
		// TODO Auto-generated method stub
		SharedPreferences sp=context.getSharedPreferences("ip", Activity.MODE_PRIVATE);
		SharedPreferences.Editor spe=sp.edit();
		if (null==sp.getString("serviceip", null)) {
			spe.putString("serviceip", Assist.serviceIP);
			spe.commit();
		}
		Assist.serviceIP=sp.getString("serviceip", null);
		System.out.println("初始化IP"+Assist.serviceIP);
	}

	/**
	 * 初始化imageloader
	 */
	@SuppressWarnings("deprecation")
	private void initImageLoader() {
		// TODO Auto-generated method stub
		MyApplication.options=new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)//防止内存溢出的，图片太多就这个。还有其他设置,如Bitmap.Config.ARGB_8888
				.displayer(new RoundedBitmapDisplayer(50))//设置成圆角图片，值越小，越偏向矩形，值越大，越接近圆
				.showStubImage(R.drawable.default_img)          // image在加载过程中，显示的图片
				.showImageOnFail(R.drawable.default_img)       // 不是图片文件 显示图片
				.showImageForEmptyUri(R.drawable.default_img)     //url为空会显示该图片
				.build();
		//初始化ImageLoader
		ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY-2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(MyApplication.options)//上面的options对象，一些属性配置
				.threadPoolSize(8)//线程池内加载的数量
				.memoryCacheSize(2*1024*1024).discCacheSize(80*1024*1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5加密
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(MyApplication.getAppContext(),5*1000,30*1000))//connectTimeout(5s),readTimeout(30s)超时时间
				.writeDebugLogs()//Remove for releaseapp
				.build();
		ImageLoader.getInstance().init(config);


	}

}
