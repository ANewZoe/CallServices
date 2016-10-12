package com.jieshoufuwu.assist;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Assist {
	public static String serviceIP = "";// 服务器IP地址
	public static Map<String, String> sType;
	public static float WIDTH = 0;// 屏幕的宽度
	public static float HEIGHT = 0;// 屏幕的高度
	public static Call_Smessage cals;
	private static Context context;

	static{
		sType=new HashMap<String, String>();
		sType.put("1", "加水");
		sType.put("2", "加饭");
		sType.put("3", "加位");
		sType.put("4", "餐具");
		sType.put("5", "纸巾");
		sType.put("6", "结账");
		sType.put("7", "签到");
		sType.put("8", "其他");
		sType.put("9", "wifi");
		sType.put("10", "抽奖");
		sType.put("11", "婴儿椅");
		sType.put("12", "其他服务");

	}

	/**
	 * 手机震动工具类
	 */

	public static void Vibrate(final Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static void Vibrate(final Context context, long[] pattern,
							   boolean isRepeat) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

	// MD5加密
	public static String getMD5(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(info.getBytes("UTF-8"));
			byte[] encryption = md5.digest();
			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(
							Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	// 等待时间
	public static int[] getlTimt(Long curTime, Long endTime) {
		Date cur = new Date(curTime);// 呼叫时间
		Date end = new Date(endTime);// 当前时间
		Long ltime = end.getTime() - cur.getTime();// 等待时间
		int t = (int) (ltime / (1000 * 60 * 60 * 24));// 天数
		int h = (int) ((ltime - t * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));// 小时
		int m = (int) ((ltime - (t * (1000 * 60 * 60 * 24) + h* (1000 * 60 * 60))) / (1000 * 60));// 分
		int s=(int)((ltime-(t * (1000 * 60 * 60 * 24) + h* (1000 * 60 * 60)+m*(1000*60)))/1000);//秒
		return new int[] { t, h, m,s};
	}



	//激活屏幕
	@SuppressWarnings("deprecation")
	public static void wakeUpAndUnlock(){
		KeyguardManager km=(KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kLock=km.newKeyguardLock("unLock");
		//解锁
		kLock.disableKeyguard();
		//获取电源管理器对象
		PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
		//获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wLock=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		//唤醒屏幕
		wLock.acquire();
		//释放
		wLock.release();
	}

	/**
	 * 获取版本名称
	 */
	public static String getVerName(Context context){
		String verName="";
		try {
			//注意：getPackageName()是你当前类的包名，0代表是获取版本信息
			//”com.example.jieshoufuwu“对应AndroidManifest.xml里的package="……"部分
			verName=context.getPackageManager().getPackageInfo("com.example.jieshoufuwu", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("msg", e.getMessage());
		}
		return verName;
	}



}
