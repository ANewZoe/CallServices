package com.jieshoufuwu.assist;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.example.jieshoufuwu.R;


public class Shortcut {

	public static void createShortCut(Activity activity,int iconResId,int appnameResId){
		Intent shortcutIntent=new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra("duplicate", false);
		//需要实现的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
		//快键图片
		Parcelable icon=Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), iconResId);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(activity.getApplicationContext(),activity.getClass()));
		activity.sendBroadcast(shortcutIntent);
	}

}
