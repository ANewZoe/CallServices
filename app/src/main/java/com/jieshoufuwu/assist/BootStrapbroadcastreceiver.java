package com.jieshoufuwu.assist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jieshoufuwu.activity.EntryActivity;


public class BootStrapbroadcastreceiver extends BroadcastReceiver{


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Intent intent2=new Intent(context,EntryActivity.class);
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent2);
	}

	public BootStrapbroadcastreceiver() {
		super();
		// TODO Auto-generated constructor stub
	}



}
